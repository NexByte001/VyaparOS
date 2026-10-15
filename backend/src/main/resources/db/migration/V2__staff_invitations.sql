-- V2: Staff Invitations and Enhanced Membership Attributes

-- 1. Add job_title and department to business_memberships
ALTER TABLE business_memberships ADD COLUMN IF NOT EXISTS job_title VARCHAR(100) NULL;
ALTER TABLE business_memberships ADD COLUMN IF NOT EXISTS department VARCHAR(100) NULL;

-- 2. Staff Invitations table for seamless new employee onboarding
CREATE TABLE IF NOT EXISTS staff_invitations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    business_id BIGINT NOT NULL,
    email VARCHAR(150) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NULL,
    role VARCHAR(30) NOT NULL,
    job_title VARCHAR(100) NULL,
    department VARCHAR(100) NULL,
    invitation_token VARCHAR(120) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    invited_by_user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    CONSTRAINT fk_invitation_business FOREIGN KEY (business_id) REFERENCES businesses(id) ON DELETE CASCADE,
    CONSTRAINT fk_invitation_invited_by FOREIGN KEY (invited_by_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_invitation_business_status ON staff_invitations(business_id, status);
CREATE INDEX idx_invitation_email ON staff_invitations(email);
