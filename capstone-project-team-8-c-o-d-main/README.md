# All-In-One Asset Manager (Team 8)

A full-stack IT asset management system designed for enterprise-level device tracking, ticket submission/approval, onboarding workflows, and offline-access support.

This system includes a **JavaFX desktop client** and a **Quarkus REST API backend**, working together with both **Microsoft SQL Server (AWS)** and **SQLite offline caching**.

---

##  Project Overview

### **Frontend — JavaFX Client**
- Desktop GUI application  
- Secure JWT login  
- Submit onboarding & retirement tickets  
- View and manage assets  
- Offline cached mode using SQLite  

### **Backend — Quarkus Server**
- REST API using Quarkus  
- JWT authentication  
- CRUD operations for tickets  
- Batch onboarding support  
- Approvals & ticket state transitions  
- SQL Server integration  

### **Databases**
- **Primary Database:** AWS EC2 – Microsoft SQL Server  

### **Tech Stack**
| Component | Technology |
|----------|------------|
| Frontend | JavaFX (Java 22+) |
| Backend | Quarkus (Java 17+) |
| Build Tool | Maven |
| Databases | SQL Server + SQLite |
| Authentication | JWT |
| Communication | REST / JSON |

---

##  High-Level Architecture

JavaFX Client <-----> Quarkus Server <-----> AWS SQL Server


---

##  Prerequisites

### **Required Software**
- Java **JDK 22+**
- Maven **3.8+**
- IntelliJ IDEA (recommended)
- Internet access (for SQL Server & login)
- Running instance of the Quarkus backend  

### **Required Ports**
| Purpose | Port |
|---------|------|
| Quarkus API | **8080** |
| SQL Server | **1433** |

### **Backend Repository**
🔗 https://github.com/DyeTry/Team8_Quarkus_Server.git

---

##  Running the JavaFX Client

### **1. Open the project in IntelliJ**

### **2. Start the Quarkus Backend**
From the backend folder:

```bash
mvn quarkus:dev
3. Run the JavaFX App
Press Run inside IntelliJ.

🎥 Development Checkpoint Video
📌 https://drive.google.com/file/d/1ZZIpGf7WeLDf8GrMFJBvOZhf4GuYMiFt/view?usp=drive_link

Manual Testing & Validation
Use this section when validating functionality before a checkpoint, sprint review, or final demo.

1. Authentication Testing
Steps
Launch the Quarkus backend.

Open the JavaFX client.

Enter valid credentials.

Confirm that login succeeds and the dashboard loads.

Attempt invalid credentials to confirm proper error handling.

Expected Results
Valid login → dashboard loads

Invalid login → descriptive error message

User permissions determined by stored role

2. Ticket System Testing
A. View Tickets
Navigate to Tickets.

Confirm all tickets load correctly.

Check fields: ticket code, name, status, equipment, etc.

Expected: All tickets display without errors.

B. Create Onboarding Ticket
Open Onboarding page.

Fill in:

Full Name

Employee ID

Email

Phone, Address

Equipment

Issue Description

Click Submit.

Expected:

New ticket appears in list

SQL Server receives the new record

Status = submitted

C. Batch Onboarding Ticket
Navigate to Batch Onboarding.

Enter user info once.

Select multiple equipment items.

Submit.

Expected:

Multiple tickets are created

Each ticket has its correct equipment label

D. Approve a Ticket
Select a ticket.

Click Approve.

Expected:

Status updates to approved

UI refreshes accordingly

E. Retirement Ticket
Go to Retirement Ticket page.

Fill in asset & employee details.

Submit.

Expected:

Ticket created with retired status

Shows properly in list view


4. Database Validation
SQL Server Validation
Using SSMS/Azure Data Studio check:

New tickets appear

Batch tickets generate correct number of rows

Status updates correctly

ticketCode and timestamps populate properly


5. Role-Based Access Testing
Role	Should See	Should Not See
Admin	All tickets, all tools	None
Manager	Tickets, asset views	Admin user controls
Tech	Assigned tickets	User/role management

6. Full End-to-End Workflow Test
Login as Admin

Submit onboarding ticket

Submit batch onboarding ticket

Approve a ticket

Submit a retirement ticket

Restart the app

Confirm all records remain correct

Expected:
A complete, clean workflow from start to finish.
