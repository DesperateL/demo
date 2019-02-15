package com.example.demo.service;

import com.example.demo.dao.LoginTicketDAO;
import com.example.demo.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketService {

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public int addTicket() {
        for (int i = 0; i < 10; i++) {
            LoginTicket ticket = new LoginTicket();
            ticket.setStatus(0);
            ticket.setUserId(i + 1);
            ticket.setExpired(new Date());
            ticket.setTicket(String.format("TICKET%d", i + 1));
            loginTicketDAO.addTicket(ticket);
        }
        return 1;

        //loginTicketDAO.updateStatus(ticket.getTicket(), 2);

    }
}

