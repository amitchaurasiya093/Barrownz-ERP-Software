package com.erp.service;

import java.util.List;

import com.erp.dto.ProjectDetailsRequestDto;
import com.erp.model.Employee;
import com.erp.model.ProjectDetails;

public interface ProjectDetailsService {

    public String createProject(ProjectDetailsRequestDto dto);

    List<ProjectDetailsRequestDto> getAllProjectDetails();

    public ProjectDetails getProjectById(int id);

    public String updateProject(int id, ProjectDetailsRequestDto dto);

    public String deleteProjectDetails(int id);

    public List<Employee> buildTeamAndCalculate(ProjectDetailsRequestDto dto, ProjectDetails tempProject);
}
