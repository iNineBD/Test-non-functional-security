-- Inserir usuário admin padrão (senha: admin123, hash gerado com BCrypt)
INSERT INTO users (username, password) VALUES (
    'admin',
    '$2b$12$uVzdInzTzOoZbUSC3DDF0u.DRY8U8sQUUU30QsI/z/WU/fSicvNRa'
);
-- Inserir usuário admin padrão (senha: user123, hash gerado com BCrypt)
INSERT INTO users (username, password) VALUES (
    'user',
    '$2a$12$UFpZuxDhkQ9QZ9VhYQomNeHQIetgW/LbZDhu.i3LmEGsyu3IoqPU2'
);




