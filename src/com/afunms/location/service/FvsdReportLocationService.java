package com.afunms.location.service;

import java.util.ArrayList;
import java.util.List;

import com.afunms.location.model.FvsdReportLocation;

public class FvsdReportLocationService {

    private static FvsdReportLocationService instance = null;

    private List<FvsdReportLocation> list = null;

    private FvsdReportLocationService() {
        list = new ArrayList<FvsdReportLocation>();
    }
    
    public static FvsdReportLocationService getInstance() {
        if (instance == null) {
            instance = new FvsdReportLocationService();
        }
        return instance;
    }
    
    public void addFvsdReportLoction(FvsdReportLocation fvsdReportLocation) {
        list.add(fvsdReportLocation);
    }

    public List<FvsdReportLocation> getAllFvsdReportLoaction() {
        return list;
    }

    public FvsdReportLocation getFvsdReportLoaction(String id) {
        FvsdReportLocation result = null;
        for (FvsdReportLocation fvsdReportLocation : list) {
            if (fvsdReportLocation.getId().equals(id)) {
                result = fvsdReportLocation;
                break;
            }
        }
        return result;
    }

    public FvsdReportLocation getDefaultFvsdReportLoaction() {
        FvsdReportLocation result = null;
        for (FvsdReportLocation fvsdReportLocation : list) {
            if (fvsdReportLocation.isDefault()) {
                result = fvsdReportLocation;
                break;
            }
        }
        return result;
    }
}
