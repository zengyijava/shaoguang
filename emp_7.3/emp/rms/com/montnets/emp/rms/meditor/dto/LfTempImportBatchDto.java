package com.montnets.emp.rms.meditor.dto;

import com.montnets.emp.rms.meditor.entity.LfTempImportBatch;

public class LfTempImportBatchDto extends LfTempImportBatch {
    /**
     * 起始时间
     */
    private String addtimeStart;
    /**
     * 结束时间
     */
    private String addtimeEnd;

    public String getAddtimeStart() {
        return addtimeStart;
    }

    public void setAddtimeStart(String addtimeStart) {
        this.addtimeStart = addtimeStart;
    }

    public String getAddtimeEnd() {
        return addtimeEnd;
    }

    public void setAddtimeEnd(String addtimeEnd) {
        this.addtimeEnd = addtimeEnd;
    }

    @Override
    public String toString() {
        return "LfTempImportBatchDto{" +
                "addtimeStart='" + addtimeStart + '\'' +
                ", addtimeEnd='" + addtimeEnd + '\'' +
                '}';
    }
}
