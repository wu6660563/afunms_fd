/**
 * <p>Description:pagination,paginates records list</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.base;

public class JspPage
{
   private int perPage;     //每页允许记录数
   private int totalRecord; //总记录数
   private int currentPage; //当前页号
   private int minNum;      //当前页的第一条记录的记录号
   private int maxNum;      // 当前页的最后一条记录的记录号
   private int totalPage;   //总页数

   public JspPage(int iPerPage,int iCurrentPage,int iTotalRecord)
   {
      init(iPerPage,iCurrentPage,iTotalRecord);
   }

   public JspPage(int iCurrentPage,int iTotalRecord)
   {
      init(15,iCurrentPage,iTotalRecord);
   }

    /**
     * 构造函数
     * @param iPerPage 每页允许记录数
     * @param iCurrentPage 当前页号
     * @param iTotalRecord 总记录数
     */
   public void init(int iPerPage,int iCurrentPage,int iTotalRecord)
   {
      perPage = iPerPage;
      currentPage = iCurrentPage;
      totalRecord = iTotalRecord;

      if( currentPage <= 0 )
        currentPage = 1;

      if( totalRecord % perPage == 0 )
         totalPage = totalRecord / perPage;
      else
         totalPage = totalRecord / perPage + 1;

      minNum = (currentPage - 1) * perPage;

      if (totalRecord % perPage != 0 && currentPage == totalPage)
         maxNum = minNum + totalRecord % perPage;
      else
         maxNum = minNum + perPage;

      minNum++;
      if (totalPage == 0) totalPage = 1;
   }

   public int getPerPage()
   {
      return perPage;
   }

    public void setPerPage(int newPerPage)
    {
      perPage = newPerPage;
    }

    public int getRecordTotal()
    {
      return totalRecord;
    }

    public void setTotalRecord(int newTotalRecord)
    {
      totalRecord = newTotalRecord;
    }

    public int getCurrentPage()
    {
      return currentPage;
    }

    public void setCurrentPage(int newCurrentPage)
    {
      currentPage = newCurrentPage;
    }

    public int getMaxNum()
    {
      return maxNum;
    }

    public void setMaxNum(int newMaxNum)
    {
      maxNum = newMaxNum;
    }

    public int getMinNum()
    {
      return minNum;
    }

    public void setMinNum(int newMinNum)
    {
      minNum = newMinNum;
    }

    public int getPageTotal()
    {
      return totalPage;
    }

    public void setTotalPage(int newTotalPage)
    {
      totalPage = newTotalPage;
    }
    
    public int getStartRow()
    {
       return minNum;
    }    
}
