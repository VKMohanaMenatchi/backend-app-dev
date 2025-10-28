-- Insert default categories
INSERT INTO categories (name, description, slug, created_date) VALUES 
('Technology', 'Posts about technology and programming', 'technology', CURRENT_TIMESTAMP),
('Lifestyle', 'Posts about lifestyle and personal experiences', 'lifestyle', CURRENT_TIMESTAMP),
('Travel', 'Posts about travel and adventures', 'travel', CURRENT_TIMESTAMP),
('Food', 'Posts about food and recipes', 'food', CURRENT_TIMESTAMP),
('Health', 'Posts about health and wellness', 'health', CURRENT_TIMESTAMP);

-- Note: Passwords will be encoded by the application during registration
-- Default users will be created through the registration endpoint