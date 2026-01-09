package org.team8.junit_testing;

import org.junit.jupiter.api.Test;
import org.team8.client.TicketDto;
import org.team8.controller.TicketSubmissionController;

import static org.junit.jupiter.api.Assertions.*;

public class TicketSubmissionControllerTest {

    @Test
    void buildOnboardingDtoCopiesAllFields() {
        TicketSubmissionController controller = new TicketSubmissionController();

        TicketDto dto = controller.buildOnboardingDto(
                "Dylan",
                "dlt27",
                "dlt27@student.uwf.edu",
                "8505551234",
                "123 Main St",
                "Laptop",
                "ThinkPad T14",
                "Needs laptop for new hire"
        );

        assertEquals("Dylan", dto.fullName);
        assertEquals("dlt27", dto.employeeId);
        assertEquals("dlt27@student.uwf.edu", dto.email);
        assertEquals("8505551234", dto.phone);
        assertEquals("123 Main St", dto.address);
        assertEquals("Laptop", dto.equipment);
        assertEquals("ThinkPad T14", dto.assetName);
        assertEquals("Needs laptop for new hire", dto.issueDescription);

        // if your controller always sets status for new tickets:
        assertEquals("SUBMITTED", dto.status);
    }
}
