package com.yuriikovalchuk.repository.report;

import com.yuriikovalchuk.domain.Report;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private Map<Integer, Map<Integer, Report>> reports = new ConcurrentHashMap<>();

    @Override
    public void add(Report report) {
        int userId = report.getUserId();
        Map<Integer, Report> userReports = reports.computeIfAbsent(userId, ConcurrentHashMap::new);
        userReports.put(report.getOrderId(), report);
    }

}
