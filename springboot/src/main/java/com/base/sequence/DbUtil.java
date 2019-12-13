package com.base.sequence;

import com.base.util.StrUtil;
import com.cmq.utils.SpringUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 数据库工具类
 */
@Slf4j
public final class DbUtil {

    private DbUtil() {
    }

    private static HashMap<String, SysSequenceHelper> seqCacheMap = new HashMap<String, SysSequenceHelper>();

    private static HashMap<String, SysSequences> seqConfigMap = new HashMap<String, SysSequences>();

    private static HashMap<String, String> tableSequnceCodeMap = new HashMap<String, String>();

    private final static String DB_TYPE_ORA = "Oracle";

    private final static String DB_TYPE_MYSQL = "Mysql";

    private static JdbcTemplate jdbcTemplate;

    private static PlatformTransactionManager transactionManager;

    private static String dbType = "Oracle";

    /**
     * 根据表名及字段名，获取在表SEQ_MANAGE中配置的序列
     *
     * @param tableCode
     * @param fieldCode
     * @return 最大值为Long.MAX_VALUE
     */
    public static Long getSequenceNumber(String tableCode, String fieldCode) {
        if (tableCode == null || tableCode.trim().isEmpty()) {
            return null;
        }
        //默认值为ID
        if (fieldCode == null || fieldCode.trim().isEmpty()) {
            fieldCode = "id";
        }
        String sequenceCode = getSequenceCode(tableCode, fieldCode);
        if (sequenceCode == null) {
            sequenceCode = "SEQ_" + tableCode;
            log.debug(
                "There is no record configured in the table SEQ_MANAGE that table_code=" + tableCode
                    + ",and field_code=" + fieldCode + ", using the default sequence_name="
                    + sequenceCode);
            //CommonException.throwOut(BaseLogModuleConsts.BASE_DB_MODULE, BaseExpCodeConsts.DB_ACCESS_EXP, StrUtil.strFormat("数据库序列没有取到，请检查序列是否正确配置.表 %s,主键 %s", tableCode, fieldCode));
        }
        return jdbcGetSeqNextval(sequenceCode);
    }

    /**
     * 根据表名及字段名，获取在表SEQ_MANAGE中配置的序列
     *
     * @param tableCode
     * @param fieldCode
     * @return 最大值为Long.MAX_VALUE
     */
    public static String getSequenceString(String tableCode, String fieldCode) {
        return String.valueOf(getSequenceNumber(tableCode, fieldCode));
    }

    /**
     * 根据序列名获取ID，小于指定长度则高位补0
     *
     * @param tableCode
     * @param fieldCode
     * @param length
     * @return
     */
    public static String getSequenceString(String tableCode, String fieldCode, int length) {
        String sequence = getSequenceString(tableCode, fieldCode);
        return StringUtils.leftPad(sequence, length, '0');
    }

    /**
     * 生成yyMMdd+8位流水+6位随机数，共20位长的主键ID
     *
     * @param tableCode
     * @param fieldCode
     * @return
     */
    public static String getCombinedSeq(String tableCode, String fieldCode) {
        String returnSequence = null;
        String seq = getSequenceString(tableCode, fieldCode, 8);
        String dataString = DateUtil.formatDate(new Date(), "yyMMdd");
        String randStr = StringUtils.leftPad(Integer.toString(RandomUtils.nextInt(999999)), 6, '0');
        returnSequence = dataString + seq + randStr;
        return returnSequence;
    }

    /**
     * 直接根据序列名取序列ID
     *
     * @param seqName 序列名称
     * @return
     */
    public static Long getSequenceNumber(String seqName) {
        return jdbcGetSeqNextval(seqName);
    }

    /**
     * 直接根据序列名取序列ID
     *
     * @param seqName 序列名称
     * @return
     */
    public static String getSequenceString(String seqName) {
        return String.valueOf(getSequenceNumber(seqName));
    }

    /**
     * 直接根据序列名取序列ID, 小于指定长度则高位补0
     *
     * @param seqName 序列名称
     * @param length  长度
     * @return
     */
    public static String getSequenceString(String seqName, int length) {
        return StringUtils.leftPad(String.valueOf(getSequenceNumber(seqName)), length, '0');
    }

    /**
     * 根据序列名获取ID
     *
     * @param seqName
     * @return
     */
    protected static long jdbcGetSeqNextval(final String seqName) {
        final String upSeqName = seqName.toUpperCase();
        if (seqConfigMap.isEmpty()) {
            loadAllSequence();
        }
        // 找到sequence配置信息
        SysSequences seqConfig = seqConfigMap.get(upSeqName);
        if (seqConfig == null) {
            //取数据库，放入map(主要应对后面配置的seq，无需重启应用)
            seqConfig = loadSequence(upSeqName);

            if (seqConfig != null) {
                seqConfigMap.put(upSeqName, seqConfig);
            } else {
                log.error(" %s IS NOT CONFIGURED IN THE SEQ_MANAGE TABLE OR THE SEQUENCE IS NOT CREATED.",
                    upSeqName);
                //数据库没有就抛异常

            }
        }

        // incrementby 为1,直接用jdbc返回
        if (seqConfig.getIncrementBy() == 1) {
            return SysSequenceHelper.jdbcGetSeqNextval(upSeqName);
        }
        // increment by >1 使用helper来缓存
        SysSequenceHelper seqHelper = seqCacheMap.get(upSeqName);
        if (seqHelper == null) {
            synchronized (seqCacheMap) {
                seqHelper = seqCacheMap.get(upSeqName);
                if (seqHelper == null) {
                    seqHelper = new SysSequenceHelper(upSeqName, seqConfig.getIncrementBy());
                    seqCacheMap.put(upSeqName, seqHelper);
                }
            }
        }
        return seqHelper.generateId();

    }

    /**
     * 方法功能: 获取数据库的SEQ .
     *
     * @param seqKey SEQ 名称
     * @return long seq取值
     */
    protected static long getOracleSequence(String seqKey) {

        StringBuffer sql = new StringBuffer("select ");
        sql.append(seqKey).append(".nextval FROM DUAL");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtils
            .getBean("jdbcTemplate");
        Long queryForLong = jdbcTemplate.queryForObject(sql.toString(), Long.class);
        if (queryForLong == null) {
            log.error("调用生成系列语句返回值为null,seq_name %s .",seqKey);
        }
        return queryForLong;
    }

    /**
     * 是否是Oracle数据库 .
     *
     * @return
     */
    public static boolean isOracle() {
        return dbType.equalsIgnoreCase(DB_TYPE_ORA);
    }

    /**
     * 是否是sql数据库 .
     *
     * @return
     */
    public static boolean isMysql() {
        return dbType.equalsIgnoreCase(DB_TYPE_MYSQL);
    }

    /**
     * 全量加载序列信息
     */
    protected static void loadAllSequence() {
        JdbcTemplate jdbc = (JdbcTemplate) SpringUtils.getBean("jdbcTemplate");

        String sql = "SELECT * FROM sys_sequences";
        if (isOracle()) {
            sql = "select t.SEQUENCE_NAME,t.MIN_VALUE as START_BY" + ",t.INCREMENT_BY,t.LAST_NUMBER"
                + ",t.INCREMENT_BY as JVM_STEP_BY " + " from user_sequences t";
        }
        final HashMap<String, SysSequences> tmpSeqMap = new HashMap<String, SysSequences>();
        final List<SysSequences> data = new ArrayList<SysSequences>();
        final RowMapper<SysSequences> rowMapper = BeanPropertyRowMapper
            .newInstance(SysSequences.class);
        jdbc.query(sql, new ResultSetExtractor<List<SysSequences>>() {
            public List<SysSequences> extractData(final ResultSet rs) throws SQLException {
                int currentRow = 0;
                while (rs.next()) {
                    SysSequences rData = rowMapper.mapRow(rs, currentRow);
                    data.add(rData);
                    tmpSeqMap.put(rData.getSequenceName().toUpperCase(), rData);
                    currentRow++;
                }
                return data;
            }
        });
        seqConfigMap = tmpSeqMap;
    }

    /**
     * 全量加载序列信息
     */
    protected static SysSequences loadSequence(String name) {
        JdbcTemplate jdbc = (JdbcTemplate) SpringUtils.getBean("jdbcTemplate");
        if (name == null) {
            return null;
        }
        String sql =
            "SELECT * FROM sys_sequences t where t.sequence_name = '" + name.toUpperCase() + "'";
        if (isOracle()) {
            sql = "select t.SEQUENCE_NAME,t.MIN_VALUE as START_BY" + ",t.INCREMENT_BY,t.LAST_NUMBER"
                + ",t.INCREMENT_BY as JVM_STEP_BY " + " from user_sequences t "
                + " where t.sequence_name = '" + name.toUpperCase() + "'";
        }
        final List<SysSequences> data = new ArrayList<SysSequences>();
        final RowMapper<SysSequences> rowMapper = BeanPropertyRowMapper
            .newInstance(SysSequences.class);
        jdbc.query(sql, new ResultSetExtractor<List<SysSequences>>() {
            public List<SysSequences> extractData(final ResultSet rs) throws SQLException {
                int currentRow = 0;
                while (rs.next()) {
                    SysSequences rData = rowMapper.mapRow(rs, currentRow);
                    data.add(rData);
                    currentRow++;
                }
                return data;
            }
        });

        if (data != null && data.size() > 0) {
            return data.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据表名及字段名，获取在表SEQ_MANAGE中配置的序列名
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public static String getSequenceCode(String tableName, String columnName) {
        String upName = (tableName + columnName).toUpperCase();
        String sequenceCode = tableSequnceCodeMap.get(upName);
        if (sequenceCode == null) {
            synchronized (tableSequnceCodeMap) {
                sequenceCode = tableSequnceCodeMap.get(upName);
                if (sequenceCode == null) {
                    JdbcTemplate jdbc = (JdbcTemplate) SpringUtils.getBean("jdbcTemplate");
                    String sql = "select a.seq_code from seq_manage a where a.table_code = ? and field_code = ? ";
                    List<String> sequenceCodeList = jdbc.query(sql,
                        new String[] {tableName.toUpperCase(), columnName.toUpperCase()},
                        new RowMapper<String>() {

                            @Override
                            public String mapRow(ResultSet arg0, int arg1) throws SQLException {
                                return arg0.getString(1);
                            }

                        });
                    if (sequenceCodeList.size() != 1) {
                        return null;
                    }
                    sequenceCode = sequenceCodeList.get(0);
                    tableSequnceCodeMap.put(upName, sequenceCode);
                }
            }
        }
        return sequenceCode;
    }

    /**
     * 根据表名及字段名，获取在表SEQ_MANAGE中配置的序列名
     *
     * @param tableName
     * @param columnName
     * @return
     */
    public static String test(String tableName) {
        String upName = (tableName).toUpperCase();
        String sequenceCode = tableSequnceCodeMap.get(upName);
        if (sequenceCode == null) {
            synchronized (tableSequnceCodeMap) {
                sequenceCode = tableSequnceCodeMap.get(upName);
                if (sequenceCode == null) {
                    JdbcTemplate jdbc = (JdbcTemplate) SpringUtils.getBean("jdbcTemplate");
                    String sql = "select a.type from tfm_config a where a.id= ? ";
                    List<String> sequenceCodeList = jdbc
                        .query(sql, new String[] {tableName.toUpperCase()},
                            new RowMapper<String>() {

                                @Override
                                public String mapRow(ResultSet arg0, int arg1) throws SQLException {
                                    return arg0.getString(1);
                                }

                            });
                    if (sequenceCodeList.size() != 1) {
                        return null;
                    }
                    sequenceCode = sequenceCodeList.get(0);
                }
            }
        }
        return sequenceCode;
    }

    public static JdbcTemplate getJdbcTemplate() {
        if (DbUtil.jdbcTemplate == null) {
            jdbcTemplate = SpringUtils.getBean("jdbcTemplate");
        }
        return jdbcTemplate;
    }

    /**
     * 提供给spring配置的时候注入jdbcTemplate
     *
     * @param jdbcTemplate
     */
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void setjdbcTemplate(JdbcTemplate jdbcTemplate) {
        DbUtil.jdbcTemplate = jdbcTemplate;
    }

    public static PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * 提供给spring配置的时候注入transactionManager
     */
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        DbUtil.transactionManager = transactionManager;
    }

    /**
     * @param dbType 设置dbType属性 1:Mysql, 2:Oracle
     */
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public  static  void   setDbType(String dbType) {
        DbUtil.dbType = dbType;
    }

    /**
     * @param pre
     * @param post
     * @param length
     * 生成编码 不足位数补0
     * @return
     */
    public static String genCode(String pre, String post, int length) {
        String code = StrUtil.strnull(pre);
        Long seq = jdbcGetSeqNextval("SEQ_COMMON");
        int tl = length - (StrUtil.strnull(pre).length() + seq.toString().length() + StrUtil
            .strnull(post).length());
        StringBuilder sb = new StringBuilder("");
        while (tl > 0) {
            sb.append("0");
            tl--;
        }
        code = code + sb.toString() + seq + StrUtil.strnull(post);
        return code;
    }

}
