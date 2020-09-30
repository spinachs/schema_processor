package com.jd.datamill9n.component.autoconfigure.table;

import lombok.Data;

/**
 * TODO
 *
 * @author maliang56
 * @date 2020-09-30
 */
@Data
public class MonitorTable {
    private Long audienceIde;
    private Integer layer;
    private String startDate;
    private String endDate;
    private String averageOrderCount90Days;
    private String averageOrderAmount90Days;
    private String averageAddCartCount90Days;
    private String averageOrderCount2Years;
    private String averageOrderDays90Days;
    private String averageViewCount90Days;
    private String top5AverageOrderCount90Days;
    private String top5AverageOrderAmount90Days;
    private String top5AverageAddCartCount90Days;
    private String top5AverageOrderCount2Years;
    private String top5AverageOrderDays90Days;
    private String top5AverageViewCount90Days;
}
