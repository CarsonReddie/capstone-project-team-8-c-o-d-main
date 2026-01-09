package org.team8.junit_testing;

import org.junit.jupiter.api.Test;
import org.team8.client.TicketDto;
import org.team8.controller.DashboardController;

import static org.junit.jupiter.api.Assertions.*;

public class DashboardRetireTicketTest {

    private TicketDto ticketWithStatus(String status) {
        TicketDto t = new TicketDto();
        t.status = status;
        return t;
    }

    // --- Role-based tests ----------------------------------------------------

    @Test
    void managerCanRetireApprovedTicket() {
        TicketDto approved = ticketWithStatus("APPROVED");

        boolean result = DashboardController.canRetireTicket("MANAGER", approved);

        assertTrue(result, "MANAGER should be allowed to retire APPROVED tickets");
    }

    @Test
    void adminCanRetireApprovedTicket() {
        TicketDto approved = ticketWithStatus("APPROVED");

        boolean result = DashboardController.canRetireTicket("ADMIN", approved);

        assertTrue(result, "ADMIN should be allowed to retire APPROVED tickets");
    }

    @Test
    void techCannotRetireEvenIfApproved() {
        TicketDto approved = ticketWithStatus("APPROVED");

        assertFalse(DashboardController.canRetireTicket("TECH", approved));
    }

    @Test
    void userCannotRetireEvenIfApproved() {
        TicketDto approved = ticketWithStatus("APPROVED");

        assertFalse(DashboardController.canRetireTicket("USER", approved));
    }

    @Test
    void nullRoleCannotRetire() {
        TicketDto approved = ticketWithStatus("APPROVED");

        assertFalse(DashboardController.canRetireTicket(null, approved));
    }

    // --- Status-based tests --------------------------------------------------

    @Test
    void managerCannotRetireSubmittedTicket() {
        TicketDto submitted = ticketWithStatus("SUBMITTED");

        boolean result = DashboardController.canRetireTicket("MANAGER", submitted);

        assertFalse(result, "Only APPROVED tickets should be retire-able");
    }

    @Test
    void adminCannotRetireSubmittedTicket() {
        TicketDto submitted = ticketWithStatus("SUBMITTED");

        boolean result = DashboardController.canRetireTicket("ADMIN", submitted);

        assertFalse(result);
    }

    @Test
    void managerCannotRetireAlreadyRetiredTicket() {
        TicketDto retired = ticketWithStatus("RETIRED");

        boolean result = DashboardController.canRetireTicket("MANAGER", retired);

        assertFalse(result, "RETIRED tickets should not be retire-able again");
    }

    @Test
    void nullTicketCannotBeRetired() {
        assertFalse(DashboardController.canRetireTicket("MANAGER", null));
        assertFalse(DashboardController.canRetireTicket("ADMIN", null));
    }
}
