CREATE DATABASE IF NOT EXISTS autonation_crm;
USE autonation_crm;

CREATE TABLE IF NOT EXISTS account_manager (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_lead (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    comment TEXT,
    account_manager_id BIGINT,
    status VARCHAR(30) NOT NULL,
    close_reason VARCHAR(30),
    idempotency_key VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_customer_lead_idempotency UNIQUE (idempotency_key),

    CONSTRAINT fk_customer_lead_manager
        FOREIGN KEY (account_manager_id)
        REFERENCES account_manager(id),

    INDEX idx_lead_name (name),
    INDEX idx_lead_email (email),
    INDEX idx_lead_phone (phone),
    INDEX idx_lead_status (status),
    INDEX idx_lead_manager (account_manager_id)
);

CREATE TABLE IF NOT EXISTS lead_status_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    old_status VARCHAR(30),
    new_status VARCHAR(30) NOT NULL,
    changed_by BIGINT,
    comments VARCHAR(500),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_status_history_lead
        FOREIGN KEY (lead_id)
        REFERENCES customer_lead(id),

    CONSTRAINT fk_status_history_user
        FOREIGN KEY (changed_by)
        REFERENCES account_manager(id),

    INDEX idx_status_history_lead (lead_id),
    INDEX idx_status_history_changed_at (changed_at)
);

CREATE TABLE IF NOT EXISTS lead_assignment_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lead_id BIGINT NOT NULL,
    old_account_manager_id BIGINT,
    new_account_manager_id BIGINT NOT NULL,
    assigned_by BIGINT,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assignment_history_lead
        FOREIGN KEY (lead_id)
        REFERENCES customer_lead(id),

    CONSTRAINT fk_assignment_history_old_manager
        FOREIGN KEY (old_account_manager_id)
        REFERENCES account_manager(id),

    CONSTRAINT fk_assignment_history_new_manager
        FOREIGN KEY (new_account_manager_id)
        REFERENCES account_manager(id),

    CONSTRAINT fk_assignment_history_assigned_by
        FOREIGN KEY (assigned_by)
        REFERENCES account_manager(id),

    INDEX idx_assignment_history_lead (lead_id),
    INDEX idx_assignment_history_assigned_at (assigned_at)
);
