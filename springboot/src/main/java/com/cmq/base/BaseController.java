package com.cmq.base;

import com.base.bean.CommonException;
import com.base.bean.ErrorResult;
import com.base.bean.GridQo;
import com.base.service.BaseService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 */
@Slf4j
public class BaseController<T, ID extends Serializable> extends
     EmptyController {
    //根据泛型注入
    @Autowired(required = false)
    BaseService<T, ID> baseService;

    /**
     * 新建实体对象.
     *
     * @param entity 新增的内容
     * @return 新增结果实体对象
     */
    @PostMapping()
    public T create(@RequestBody T entity) {
        return baseService.insert(entity);
    }

    /**
     * 根据ID获取数据.
     *
     * @param id id参数
     * @return 实体对象
     * 调用 ：http://my:9082/springboot/16.do
     * 其中.do为 registration.addUrlMappings("*.do");
     */
    @GetMapping(value = "/{id}")
    public T get(@PathVariable("id") ID id) throws Exception {

        T entity = baseService.selectByPrimaryKey(id);
        if (entity == null) {
            throw new Exception("没有找到相应的数据");
        }
        return entity;
    }

    /**
     * 根据主键ID删除数据.
     *
     * @param id 主键id
     * @return 删除的数量
     */
    @DeleteMapping(value = "/{id}")
    public int delete(@PathVariable("id") ID id) {
        if (id == null) {
            return 0;
        }
        if (id instanceof String && id.toString().trim().length() == 0) {
            return 0;
        }
        return baseService.deleteByPrimaryKey(id);
    }

    /**
     * 更新实体对象.
     *
     * @param entity 要更新的对象信息
     * @return 更新数量
     */
    @PutMapping
    public int update(@RequestBody T entity) {
        return baseService.updateByPrimaryKeySelective(entity);
    }

    /**
     * 查询所有数据.
     *
     * @return 所有结果列表
     */
    @GetMapping(value = "/selectAll")
    public List<T> selectAll() {
        return baseService.selectAll();
    }

    /**
     * 返回的包括分页信息.
     *
     * @param query    查询条件
     * @param pageNum  第几页，从1开始
     * @param pageSize 每页数量
     * @return 带有分页信息的结果对象
     */
    @PostMapping(value = "/selectPageInfo")
    public PageInfo<T> selectPageInfo(@RequestBody T query, int pageNum, int pageSize) {
        return baseService.selectPageInfo(query, pageNum, pageSize);
    }

    /**
     * 返回的列表不包括分页信息.
     *
     * @param query    查询条件
     * @param pageNum  第几页,从1开始
     * @param pageSize 每页数量
     * @return 列表结果
     */
    @PostMapping(value = "/selectPageList")
    public List<T> selectPageList(@RequestBody T query, int pageNum, int pageSize) {
        return baseService.selectPageList(query, pageNum, pageSize);
    }

    /**
     * 返回的列表不包括分页信息.
     *
     * @param query 查询对象
     * @return 查询结果列表
     */
    @PostMapping(value = "/selectList")
    public List<T> selectList(@RequestBody T query) {
        return baseService.select(query);
    }

    /**
     * 返回的列表不包括分页信息.
     *
     * @param query 表格查询条件
     * @return 返回带有分页信息的查询结果
     */
    @PostMapping(value = "/selectGridData")
    public PageInfo<T> selectGridData(@RequestBody GridQo query) {
        return baseService.selectGridData(query);
    }

    /**
     * Bss异常.
     *
     * @param request http请求对象
     * @param ex      异常信息
     * @return ErrorResult
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResult exp(HttpServletRequest request, CommonException ex) {
        log.warn(request.getRequestURI(), ex);

        ErrorResult resp = new ErrorResult();
        resp.setCode(ex.getFailCode());
        resp.setType(1);
        resp.setMessage(ex.getFailMsg());
        resp.setDate(ex.getDate());
        resp.setStack(ExceptionUtils.getStackTrace(ex));
        resp.setLogModule(ex.getLogModule());
        resp.setMessageH(ex.getMassageH());
        return resp;
    }

    /**
     * qryExportData:通过反射查询方法，查询对应数据.<br/>
     * 注意：反正查询方法必须为如下格式
     * public List<Object> qryXXX(Map<String, Object> param) {
     * .....
     * return list;
     * }
     *
     * @return
     * @author denghankun
     * @since JDK 1.7
     */
    private List<Object> qryExportDataByReflect(Map<String, Object> param) {
        String methodName = (String) param.get("method");
        if (StringUtils.isEmpty(methodName)) {
            throw new IllegalArgumentException("请求中必须含有[method]参数并且不为空");
        }
        String className = this.getClass().getName();
        try {
            Method method = this.getClass().getMethod(methodName, Map.class);
            if (Modifier.isPublic(method.getModifiers())) {
                Object r = method.invoke(this, param);
                return ((List<Object>) r);
            }
            throw new RuntimeException(String
                .format("Controller.method[%s.%s]没有权限访问，请检查是否为public方法", className, methodName));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Controller[" + className + "]中没有定义名为" + methodName + "的方法");
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String
                .format("Controller.method[%s.%s]没有权限访问，请检查是否为public方法", className, methodName));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                String.format("Controller.method[%s.%s]入参不正确，只能接受Map的入参", className, methodName));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * checkExportExcelArgs:检查http请求参数的正确性，如果不正确，直接抛出RuntimeException. <br/>
     *
     * @param cols
     * @author denghankun
     * @since JDK 1.7
     */
    protected void checkExportExcelArgs(List<Map<String, String>> cols) {
        if (cols == null) {
            throw new RuntimeException("请求中必须含有cols对象");
        }
        for (Map<String, String> field : cols) {
            if (!field.containsKey("name") || !field.containsKey("label")) {
                throw new RuntimeException("请求中的cols对象必须含有name和label属性");
            }
        }
    }

    /**
     * getFileUploadPath:如果需要修改上传目录，请覆盖这个方法，指定文件的上传目录，
     * 否则统一使用系统临时目录{@code java.io.tmpdir}作为上传目录. <br/>
     *
     * @return
     * @author denghankun
     * @since JDK 1.7
     */
    protected String getFileUploadRepository() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * getExportFileName:获取导出的Excel的文件名. <br/>
     *
     * @return
     * @author denghankun
     * @since JDK 1.7
     */
    protected String getExportExcelFileName() {
        return "Excel.xls";
    }
}
