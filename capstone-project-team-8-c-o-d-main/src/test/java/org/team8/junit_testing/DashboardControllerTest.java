package org.team8.junit_testing;

import org.junit.jupiter.api.Test;
import org.team8.client.TicketDto;
import org.team8.controller.DashboardController;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardControllerTest {

    @Test
    void managerRoleCanApprove() {
        assertTrue(DashboardController.canRoleApprove("MANAGER"));
        assertTrue(DashboardController.canRoleApprove("manager"));
    }

    @Test
    void adminRoleCanApprove() {
        assertTrue(DashboardController.canRoleApprove("ADMIN"));
    }

    @Test
    void techAndUserCannotApprove() {
        assertFalse(DashboardController.canRoleApprove("TECH"));
        assertFalse(DashboardController.canRoleApprove("USER"));
        assertFalse(DashboardController.canRoleApprove(null));
    }

    @Test
    void managerCanApproveSubmittedTicket() {
        TicketDto t = new TicketDto();
        t.status = "SUBMITTED";

        assertTrue(DashboardController.canApproveTicket("MANAGER", t));
    }

    @Test
    void managerCannotApproveAlreadyApprovedTicket() {
        TicketDto t = new TicketDto();
        t.status = "APPROVED";

        assertFalse(DashboardController.canApproveTicket("MANAGER", t));
    }

    @Test
    void nonManagerNonAdminCannotApproveEvenIfSubmitted() {
        TicketDto t = new TicketDto();
        t.status = "SUBMITTED";

        assertFalse(DashboardController.canApproveTicket("TECH", t));
        assertFalse(DashboardController.canApproveTicket("USER", t));
    }

    @Test
    void nullTicketCannotBeApproved() {
        assertFalse(DashboardController.canApproveTicket("MANAGER", null));
    }
}
