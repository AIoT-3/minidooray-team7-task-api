package com.nhnacademy.task.entity;

import com.nhnacademy.task.dto.req.MileStoneCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MilestoneEntityTest {

    @Mock
    private ProjectEntity mockProject;

    @Mock
    private TaskEntity mockTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockProject.getMilestoneList()).thenReturn(new ArrayList<>());
    }

    @Test
    void testMilestoneEntityCreation() {
        String name = "Test Milestone";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);

        MilestoneEntity milestone = new MilestoneEntity(name, mockProject, startDate, endDate);

        assertThat(milestone.getName()).isEqualTo(name);
        assertThat(milestone.getProject()).isEqualTo(mockProject);
        assertThat(milestone.getStartDate()).isEqualTo(startDate);
        assertThat(milestone.getEndDate()).isEqualTo(endDate);
        assertThat(milestone.getCreatedAt()).isNotNull();
    }

    @Test
    void testSetProject() {
        MilestoneEntity milestone = new MilestoneEntity("Milestone", null, null, null);

        milestone.setProject(mockProject);

        assertThat(milestone.getProject()).isEqualTo(mockProject);
        assertThat(mockProject.getMilestoneList()).contains(milestone);

        milestone.setProject(null);
        assertThat(milestone.getProject()).isNull();
    }

    @Test
    void testUpdateNameAndStartDateAndEndDate() {
        MilestoneEntity milestone = new MilestoneEntity("Old Name", mockProject, null, null);
        String newName = "New Name";
        LocalDateTime newStartDate = LocalDateTime.now();
        LocalDateTime newEndDate = LocalDateTime.now().plusDays(10);

        milestone.updateNameAndStartDateAndEndDate(newName, newStartDate, newEndDate);

        assertThat(milestone.getName()).isEqualTo(newName);
        assertThat(milestone.getStartDate()).isEqualTo(newStartDate);
        assertThat(milestone.getEndDate()).isEqualTo(newEndDate);
    }

    @Test
    void testCreateFromRequest() {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(5);
        MileStoneCreateRequest request = new MileStoneCreateRequest("Request Name", startDate, endDate);

        MilestoneEntity milestone = MilestoneEntity.create(request, mockProject);

        assertThat(milestone.getName()).isEqualTo("Request Name");
        assertThat(milestone.getProject()).isEqualTo(mockProject);
        assertThat(milestone.getStartDate()).isEqualTo(startDate);
        assertThat(milestone.getEndDate()).isEqualTo(endDate);
    }
}
