package com.base.sequence;

import com.cmq.utils.SpringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

import static org.nutz.dao.util.Pojos.log;

/**
 * sys_sequence 缓存器.
 * 
 * @version Revision 1.0.0
 * @see:
 * @创建日期:2015-4-9
 * @功能说明:
 */
public class SysSequenceHelper {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SysSequenceHelper.class);

    private ReentrantLock lodingLock = new ReentrantLock();

    private Stack<Long> idDeque = new Stack<Long>();

    private String seqName;

    private int incrementBy;

    protected SysSequenceHelper(String seqName, int incrementBy) {
        this.seqName = seqName;
        this.incrementBy = incrementBy;
    }

    protected void loadIds()  {
        lodingLock.lock();
        try {
            if (idDeque.size() == 0) {
                long nextId = jdbcGetSeqNextval(seqName);
                if (nextId == 0) {
                    LOGGER.error("get SEQ_NG_TRANS_ID return 0, it seems no such sequence in the database.");
                    //throw new Exception("sequence不存在或者连的数据库不对");
                }
                Stack<Long> tmpDeque = new Stack<Long>();
                for (int i = 0; i < incrementBy; i++) {
                    tmpDeque.add(nextId - i);
                }
                idDeque = tmpDeque;
            }
        }
        finally {
            lodingLock.unlock();
        }

    }

    protected long generateId() {
        long id = 0;

        try {
            id = idDeque.pop();
        }
        catch (EmptyStackException e) {
            loadIds();
            return generateId();
//            try {
//                id = idDeque.pop();
//            }
//            catch (EmptyStackException ee) {
//                //CommonException.throwOut("baseDataService",BaseExpCodeConsts.UN_IO_EXP, "unable to load ids, check the error log please.", ee);
//                //序列被用完了，再尝试加载一次
//                loadIds();
//                return generateId();
//            }
        }

        return id;

    }

    protected static long jdbcGetSeqNextval(final String seqName) {
        
        if (DbUtil.isOracle()) {
            return DbUtil.getOracleSequence(seqName);
        }
        //修改为coreTransactionTemplate
        CoreTransactionTemplate tranTemp = new CoreTransactionTemplate(DbUtil.getTransactionManager());
        tranTemp.setTimeout(20);
        tranTemp.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        long id = tranTemp.execute(new TransactionCallback<Long>() {
            public Long doInTransaction(TransactionStatus status) {
                StringBuffer sql = new StringBuffer("select seq_nextval(?) FROM DUAL");
                //全局序列的数据源独立
                JdbcTemplate jdbcTemplate = (JdbcTemplate) SpringUtils.getBean("jdbcTemplate");
                Long queryForLong = jdbcTemplate.queryForObject(sql.toString(), Long.class, seqName);
                if (queryForLong == null) {
                    log.error("调用生成系列语句返回值为null,seq_name=" + seqName);
                }
                return queryForLong;
            }
        });

        return id;

    }
    
}
