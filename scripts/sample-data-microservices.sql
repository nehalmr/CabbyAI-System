-- Sample Users
USE cabbyai_user_db;
INSERT INTO users (name, email, phone, password_hash) VALUES
('Alice Cooper', 'alice@example.com', '+1555000001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa'),
('Bob Johnson', 'bob@example.com', '+1555000002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa'),
('Carol Smith', 'carol@example.com', '+1555000003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa');

-- Sample Drivers
USE cabbyai_driver_db;
INSERT INTO drivers (name, phone, license_number, vehicle_details, status, current_latitude, current_longitude) VALUES
('John Smith', '+1234567890', 'DL123456789', 'Toyota Camry 2020 - Blue', 'AVAILABLE', 40.7128, -74.0060),
('Sarah Johnson', '+1234567891', 'DL987654321', 'Honda Accord 2019 - White', 'AVAILABLE', 40.7589, -73.9851),
('Mike Davis', '+1234567892', 'DL456789123', 'Nissan Altima 2021 - Black', 'AVAILABLE', 40.7505, -73.9934),
('Emily Brown', '+1234567893', 'DL789123456', 'Hyundai Elantra 2020 - Silver', 'OFFLINE', 40.7282, -73.7949),
('David Wilson', '+1234567894', 'DL321654987', 'Chevrolet Malibu 2019 - Red', 'BUSY', 40.6892, -74.0445);

-- Sample Rides
USE cabbyai_ride_db;
INSERT INTO rides (user_id, driver_id, pickup_location, dropoff_location, estimated_fare, actual_fare, status, started_at, completed_at) VALUES
(1, 1, '123 Main St, Downtown', '456 Oak Ave, Uptown', 15.50, 15.50, 'COMPLETED', '2024-01-15 10:30:00', '2024-01-15 11:00:00'),
(2, 2, '789 Pine Rd, Westside', '321 Elm St, Eastside', 22.75, 22.75, 'COMPLETED', '2024-01-15 14:15:00', '2024-01-15 14:45:00'),
(1, 3, '555 Broadway, Central', '777 Park Ave, North', 18.25, NULL, 'IN_PROGRESS', '2024-01-15 16:00:00', NULL);

-- Sample Payments
USE cabbyai_payment_db;
INSERT INTO payments (ride_id, user_id, amount, method, status, transaction_id, gateway_response) VALUES
(1, 1, 15.50, 'CREDIT_CARD', 'COMPLETED', 'TXN_001_ABC123', 'Payment processed successfully'),
(2, 2, 22.75, 'DEBIT_CARD', 'COMPLETED', 'TXN_002_DEF456', 'Payment processed successfully');

-- Sample Ratings
USE cabbyai_rating_db;
INSERT INTO ratings (ride_id, from_user_id, to_user_id, score, comments, type) VALUES
(1, 1, 1, 5, 'Excellent driver, very professional and punctual!', 'USER_TO_DRIVER'),
(2, 2, 2, 4, 'Good ride, clean car and safe driving.', 'USER_TO_DRIVER'),
(1, 1, 1, 5, 'Polite passenger, easy pickup and dropoff.', 'DRIVER_TO_USER');
