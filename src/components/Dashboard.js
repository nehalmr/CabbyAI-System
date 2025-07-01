"use client"
import "./Dashboard.css"

const Dashboard = ({ user, onViewChange }) => {
  return (
    <div className="dashboard">
      <div className="welcome-section">
        <h2>Welcome back, {user.name}!</h2>
        <p>Ready for your next ride?</p>
      </div>

      <div className="dashboard-grid">
        <div className="dashboard-card" onClick={() => onViewChange("booking")}>
          <div className="card-icon">🚗</div>
          <h3>Book a Ride</h3>
          <p>Find and book your next cab ride</p>
          <button className="card-button">Book Now</button>
        </div>

        <div className="dashboard-card" onClick={() => onViewChange("history")}>
          <div className="card-icon">📋</div>
          <h3>Ride History</h3>
          <p>View your past rides and receipts</p>
          <button className="card-button">View History</button>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">⭐</div>
          <h3>Rate Drivers</h3>
          <p>Rate and review your recent rides</p>
          <button className="card-button">Rate Now</button>
        </div>

        <div className="dashboard-card">
          <div className="card-icon">👤</div>
          <h3>Profile</h3>
          <p>Manage your account settings</p>
          <button className="card-button">Edit Profile</button>
        </div>
      </div>

      <div className="quick-stats">
        <div className="stat-item">
          <span className="stat-number">0</span>
          <span className="stat-label">Total Rides</span>
        </div>
        <div className="stat-item">
          <span className="stat-number">$0.00</span>
          <span className="stat-label">Total Spent</span>
        </div>
        <div className="stat-item">
          <span className="stat-number">0</span>
          <span className="stat-label">Favorite Drivers</span>
        </div>
      </div>
    </div>
  )
}

export default Dashboard
