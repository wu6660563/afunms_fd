/**
 * <p>Description:loading db nodes</p>
 * <p>Company: dhcc.com</p>
 * @author miiwill
 * @project afunms
 * @date 2007-1-8
 */

package com.afunms.polling.loader;

import com.afunms.polling.base.Node;
import java.util.List;

import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.model.FTPConfig;
import com.afunms.common.base.BaseVo;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.NodeLoader;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Host;
import com.afunms.topology.model.HostNode;

public class FtpLoader extends NodeLoader {
    public void loading() {
        FTPConfigDao dao = new FTPConfigDao();
        List list = dao.loadAll();
        clearRubbish(list);
        for (int i = 0; i < list.size(); i++) {
            FTPConfig vo = (FTPConfig) list.get(i);
            loadOne(vo);
        }
    }

    public void clearRubbish(List baseVoList) {
        List nodeList = PollingEngine.getInstance().getFtpList(); // 得到内存中的list
        for (int index = 0; index < nodeList.size(); index++) {
            if (nodeList.get(index) instanceof Ftp) {
                Ftp node = (Ftp) nodeList.get(index);
                if (baseVoList == null) {
                    nodeList.remove(node);
                } else {
                    boolean flag = false;
                    for (int j = 0; j < baseVoList.size(); j++) {
                        FTPConfig hostNode = (FTPConfig) baseVoList.get(j);
                        if (node.getId() == hostNode.getId()) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        nodeList.remove(node);
                    }
                }
            }
        }
    }

    public void loadOne(BaseVo baseVo) {
        FTPConfig vo = (FTPConfig) baseVo;
        Ftp ftp = new Ftp();
        ftp.setId(vo.getId());
        ftp.setAlias(vo.getName());
        ftp.setFilename(vo.getFilename());
        ftp.setUsername(vo.getUsername());
        ftp.setPassword(vo.getPassword());
        ftp.setMonflag(vo.getMonflag());
        ftp.setTimeout(vo.getTimeout());
        ftp.setAlias(vo.getName());
        ftp.setSendemail(vo.getSendemail());
        ftp.setSendmobiles(vo.getSendmobiles());
        ftp.setSendphone(vo.getSendphone());
        ftp.setBid(vo.getBid());
        // ftp.setMonflag(vo.getMonflag());
        ftp.setIpAddress(vo.getIpaddress());
        ftp.setCategory(58);
        ftp.setStatus(0);
        ftp.setType("FTP");
        if (vo.getMonflag() == 1) {
            ftp.setManaged(true);
        } else {
            ftp.setManaged(false);
        }

        // ---------------加载被监视对象-------------------

        // dbNode.setMoidList(moidList);
        // PollingEngine.getInstance().addNode(dbNode);
        // PollingEngine.getInstance().addFtp(ftp);

        Node node = PollingEngine.getInstance().getFtpByID(ftp.getId());
        if (node != null) {
            PollingEngine.getInstance().getFtpList().remove(node);
        }
        PollingEngine.getInstance().addFtp(ftp);
    }

    public Ftp loadOneById(String id) {
        FTPConfig vo = null;
        FTPConfigDao dao = new FTPConfigDao();
        try {
            vo = (FTPConfig) dao.findByID(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dao.close();
        }
        if (vo == null) {
            return null;
        }
        Ftp ftp = new Ftp();
        ftp.setId(vo.getId());
        ftp.setAlias(vo.getName());
        ftp.setFilename(vo.getFilename());
        ftp.setUsername(vo.getUsername());
        ftp.setPassword(vo.getPassword());
        ftp.setMonflag(vo.getMonflag());
        ftp.setTimeout(vo.getTimeout());
        ftp.setAlias(vo.getName());
        ftp.setSendemail(vo.getSendemail());
        ftp.setSendmobiles(vo.getSendmobiles());
        ftp.setSendphone(vo.getSendphone());
        ftp.setBid(vo.getBid());
        // ftp.setMonflag(vo.getMonflag());
        ftp.setIpAddress(vo.getIpaddress());
        ftp.setCategory(58);
        ftp.setStatus(0);
        ftp.setType("FTP");
        ftp.setPort(vo.getPort());
        if (vo.getMonflag() == 1) {
            ftp.setManaged(true);
        } else {
            ftp.setManaged(false);
        }

        Node node = PollingEngine.getInstance().getFtpByID(ftp.getId());
        if (node != null) {
            PollingEngine.getInstance().getFtpList().remove(node);
        }
        PollingEngine.getInstance().addFtp(ftp);
        return ftp;
    } 
}