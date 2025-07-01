"use client"

import { useState } from "react"
import "./RideBooking.css"

const RideBooking = ({ user }) => {
  const [bookingData, setBookingData] = useState({
    pickupLocation: "",
    dropoffLocation: "",
  })
  const [fareEstimate, setFareEstimate] = useState(null)
  const [loading, setLoading] = useState(false)
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState("")

  const getAuthHeaders = () => {
    const token = localStorage.getItem("cabbyai_token")
    return {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    }
  }

  const handleInputChange = (e) => {
    setBookingData({
      ...bookingData,
      [e.target.name]: e.target.value,
    })
    setError("")
  }

  const calculateFare = async () => {
    if (bookingData.pickupLocation && bookingData.dropoffLocation) {
      try {
        const response = await fetch("http://localhost:8080/api/rides/estimate-fare", {
          method: "POST",
          headers: getAuthHeaders(),
          body: JSON.stringify({
            pickupLocation: bookingData.pickupLocation,
            dropoffLocation: bookingData.dropoffLocation,
          }),
        })

        if (response.ok) {
          const data = await response.json()
          setFareEstimate(data.estimatedFare)
        } else if (response.status === 401) {
          setError("Session expired. Please login again.")
        }
      } catch (err) {
        console.error("Failed to calculate fare:", err)
        setFareEstimate(15.5) // Fallback fare
      }
    }
  }

  const handleBookRide = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError("")

    try {
      const response = await fetch("http://localhost:8080/api/rides/book", {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({
          userId: user.userId,
          pickupLocation: bookingData.pickupLocation,
          dropoffLocation: bookingData.dropoffLocation,
        }),
      })

      if (response.ok) {
        const rideData = await response.json()

        // Process payment automatically
        await processPayment(rideData.rideId, rideData.estimatedFare)

        setSuccess(true)
        setBookingData({ pickupLocation: "", dropoffLocation: "" })
        setFareEstimate(null)
      } else if (response.status === 401) {
        setError("Session expired. Please login again.")
      } else {
        const errorData = await response.json()
        setError(errorData.message || "Failed to book ride. Please try again.")
      }
    } catch (err) {
      setError("Network error. Please try again.")
    } finally {
      setLoading(false)
    }
  }

  const processPayment = async (rideId, amount) => {
    try {
      await fetch("http://localhost:8080/api/payments/process", {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify({
          rideId: rideId,
          userId: user.userId,
          amount: amount,
          method: "CREDIT_CARD",
        }),
      })
    } catch (err) {
      console.error("Payment processing failed:", err)
    }
  }

  if (success) {
    return (
      <div className="booking-success">
        <div className="success-icon">✅</div>
        <h2>Ride Booked Successfully!</h2>
        <p>Your driver will arrive shortly. Payment has been processed automatically.</p>
        <button className="book-another-btn" onClick={() => setSuccess(false)}>
          Book Another Ride
        </button>
      </div>
    )
  }

  return (
    <div className="ride-booking">
      <div className="booking-header">
        <h2>Book Your Ride</h2>
        <p>Enter your pickup and destination locations</p>
      </div>

      <form onSubmit={handleBookRide} className="booking-form">
        <div className="location-inputs">
          <div className="form-group">
            <label htmlFor="pickup">Pickup Location</label>
            <input
              type="text"
              id="pickup"
              name="pickupLocation"
              value={bookingData.pickupLocation}
              onChange={handleInputChange}
              onBlur={calculateFare}
              placeholder="Enter pickup address"
              required
              maxLength="255"
            />
            <div className="location-icon pickup-icon">📍</div>
          </div>

          <div className="form-group">
            <label htmlFor="dropoff">Destination</label>
            <input
              type="text"
              id="dropoff"
              name="dropoffLocation"
              value={bookingData.dropoffLocation}
              onChange={handleInputChange}
              onBlur={calculateFare}
              placeholder="Enter destination address"
              required
              maxLength="255"
            />
            <div className="location-icon destination-icon">🎯</div>
          </div>
        </div>

        {fareEstimate && (
          <div className="fare-estimate">
            <h3>Estimated Fare</h3>
            <div className="fare-amount">${fareEstimate.toFixed(2)}</div>
            <p className="fare-note">Final fare may vary based on actual distance and time</p>
          </div>
        )}

        {error && <div className="error-message">{error}</div>}

        <div className="ride-options">
          <div className="vehicle-type">
            <h4>Select Vehicle Type</h4>
            <div className="vehicle-options">
              <label className="vehicle-option">
                <input type="radio" name="vehicleType" value="standard" defaultChecked />
                <span className="vehicle-info">
                  <span className="vehicle-name">Standard</span>
                  <span className="vehicle-desc">4 seats • Affordable</span>
                </span>
              </label>
              <label className="vehicle-option">
                <input type="radio" name="vehicleType" value="premium" />
                <span className="vehicle-info">
                  <span className="vehicle-name">Premium</span>
                  <span className="vehicle-desc">4 seats • Comfortable</span>
                </span>
              </label>
            </div>
          </div>
        </div>

        <button
          type="submit"
          className="book-ride-btn"
          disabled={loading || !bookingData.pickupLocation || !bookingData.dropoffLocation}
        >
          {loading ? "Booking..." : "Book Ride"}
        </button>
      </form>
    </div>
  )
}

export default RideBooking
