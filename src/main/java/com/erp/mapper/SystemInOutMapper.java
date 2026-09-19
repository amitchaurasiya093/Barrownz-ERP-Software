package com.erp.mapper;

import com.erp.dto.SystemInOutDto;
import com.erp.model.SystemInOut;

public class SystemInOutMapper {

    public static SystemInOutDto convertToDto(SystemInOut systemInOut) {
        SystemInOutDto dto = new SystemInOutDto();

        dto.setSystemInOutId(systemInOut.getSystemInOutId());
        dto.setSystemIn(systemInOut.getSystemIn());
        dto.setSystemOut(systemInOut.getSystemOut());

        if (systemInOut.getEmployee() != null) {
            dto.setEmployeeId(systemInOut.getEmployee().getEmpId());
            dto.setEmployeeName(
                    systemInOut.getEmployee().getFirstName() + " " + systemInOut.getEmployee().getLastName());

        }

        return dto;
    }

    public static SystemInOut convertToEntity(SystemInOutDto dto) {
        SystemInOut systemInOut = new SystemInOut();

        systemInOut.setSystemInOutId(dto.getSystemInOutId());
        systemInOut.setSystemIn(dto.getSystemIn());
        systemInOut.setSystemOut(dto.getSystemOut());

        return systemInOut;
    }

}
