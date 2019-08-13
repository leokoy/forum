package entity;

import java.util.List;

/**
 * 分页对象
 * 例子：返回分页
 *   new Result(true,StatusCode.OK,"分页查询成功",new PageResult<Label>(10,rows))
 */
public class PageResult<T> {
    /**
     * 分页总记录数
     */
     private Long total;
    /**
     * 当前页码需显示的数据
     */
    private List<T> rows;

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
