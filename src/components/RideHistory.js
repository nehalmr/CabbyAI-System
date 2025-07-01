"use client"

import { useState, useEffect } from "react"
import "./RideHistory.css"

const RideHistory = ({ user }) => {
  const [rides, setRides] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState("")

  const getAuthHeaders = () => {
    const token = localStorage.getItem("cabbyai_token")
    return {
      Authorization: `Bearer ${token}`,
    }
  }

  useEffect(() => {
    fetchRideHistory()
  }, [user.userId])

  const fetchRideHistory = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/rides/user/${user.userId}`, {
        headers: getAuthHeaders(),
      })

      if (response.ok) {
        const data = await response.json()
        setRides(data)
      } else if (response.status === 401) {
        setError("Session expired. Please login again.")
      } else {
        setError("Failed to fetch ride history")
      }
    } catch (err) {
      console.error("Failed to fetch ride history:", err)
      setError("Network error occurred")
    } finally {
      setLoading(false)
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case "COMPLETED":
        return "#008080"
      case "IN_PROGRESS":
        return "#00008B"
      case "CANCELLED":
        return "#dc3545"
      default:
        return "#6c757d"
    }
  }

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading your ride history...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">{error}</div>
        <button onClick={fetchRideHistory} className="retry-btn">
          Retry
        </button>
      </div>
    )
  }

  return (
    <div className="ride-history">
      <div className="history-header">
        <h2>Your Ride History</h2>
        <p>View all your past rides and receipts</p>
      </div>

      {rides.length === 0 ? (
        <div className="no-rides">
          <div className="no-rides-icon">🚗</div>
          <h3>No rides yet</h3>
          <p>Book your first ride to see it here!</p>
        </div>
      ) : (
        <div className="rides-list">
          {rides.map((ride) => (
            <div key={ride.rideId} className="ride-card">
              <div className="ride-header">
                <div className="ride-id">Ride #{ride.rideId}</div>
                <div className="ride-status" style={{ color: getStatusColor(ride.status) }}>
                  {ride.status}
                </div>
              </div>

              <div className="ride-locations">
                <div className="location-item">
                  <span className="location-icon">📍</span>
                  <div className="location-details">
                    <span className="location-label">From</span>
                    <span className="location-address">{ride.pickupLocation}</span>
                  </div>
                </div>
                <div className="location-divider">↓</div>
                <div className="location-item">
                  <span className="location-icon">🎯</span>
                  <div className="location-details">
                    <span className="location-label">To</span>
                    <span className="location-address">{ride.dropoffLocation}</span>
                  </div>
                </div>
              </div>

              <div className="ride-footer">
                <div className="ride-date">
                  {new Date(ride.createdAt).toLocaleDateString("en-US", {
                    year: "numeric",
                    month: "short",
                    day: "numeric",
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                </div>
                <div className="ride-fare">${ride.actualFare || ride.estimatedFare}</div>
              </div>

              {ride.status === "COMPLETED" && (
                <div className="ride-actions">
                  <button className="action-btn rate-btn">Rate Driver</button>
                  <button className="action-btn receipt-btn">View Receipt</button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default RideHistory
