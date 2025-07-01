-- Insert Sample Drivers
INSERT INTO drivers (name, phone, license_number, vehicle_details, status) VALUES
('John Smith', '+1234567890', 'DL123456789', 'Toyota Camry 2020 - Blue', 'AVAILABLE'),
('Sarah Johnson', '+1234567891', 'DL987654321', 'Honda Accord 2019 - White', 'AVAILABLE'),
('Mike Davis', '+1234567892', 'DL456789123', 'Nissan Altima 2021 - Black', 'AVAILABLE'),
('Emily Brown', '+1234567893', 'DL789123456', 'Hyundai Elantra 2020 - Silver', 'OFFLINE'),
('David Wilson', '+1234567894', 'DL321654987', 'Chevrolet Malibu 2019 - Red', 'BUSY');

-- Insert Sample Users (passwords are 'password123' hashed with BCrypt)
INSERT INTO users (name, email, phone, password_hash) VALUES
('Alice Cooper', 'alice@example.com', '+1555000001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa'),
('Bob Johnson', 'bob@example.com', '+1555000002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa'),
('Carol Smith', 'carol@example.com', '+1555000003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa');

-- Insert Sample Rides
INSERT INTO rides (user_id, driver_id, pickup_location, dropoff_location, fare, status) VALUES
(1, 1, '123 Main St, Downtown', '456 Oak Ave, Uptown', 15.50, 'COMPLETED'),
(2, 2, '789 Pine Rd, Westside', '321 Elm St, Eastside', 22.75, 'COMPLETED'),
(1, 3, '555 Broadway, Central', '777 Park Ave, North', 18.25, 'IN_PROGRESS');

-- Insert Sample Payments
INSERT INTO payments (ride_id, user_id, amount, method, status) VALUES
(1, 1, 15.50, 'CREDIT_CARD', 'COMPLETED'),
(2, 2, 22.75, 'DEBIT_CARD', 'COMPLETED');

-- Insert Sample Ratings
INSERT INTO ratings (ride_id, from_user_id, to_user_id, score, comments) VALUES
(1, 1, 1, 5, 'Excellent driver, very professional and punctual!'),
(2, 2, 2, 4, 'Good ride, clean car and safe driving.');
