"use client"

import { useState, useEffect } from "react"
import "./App.css"
import LoginForm from "./components/LoginForm"
import Dashboard from "./components/Dashboard"
import RideBooking from "./components/RideBooking"
import RideHistory from "./components/RideHistory"

function App() {
  const [user, setUser] = useState(null)
  const [currentView, setCurrentView] = useState("dashboard")

  useEffect(() => {
    // Check if user is logged in from localStorage
    const savedUser = localStorage.getItem("cabbyai_user")
    const token = localStorage.getItem("cabbyai_token")

    if (savedUser && token) {
      setUser(JSON.parse(savedUser))
    }
  }, [])

  const handleLogin = (userData) => {
    setUser(userData)
    localStorage.setItem("cabbyai_user", JSON.stringify(userData))
    // Token is already stored in LoginForm component
  }

  const handleLogout = () => {
    setUser(null)
    localStorage.removeItem("cabbyai_user")
    localStorage.removeItem("cabbyai_token")
    setCurrentView("dashboard")
  }

  const renderCurrentView = () => {
    switch (currentView) {
      case "booking":
        return <RideBooking user={user} />
      case "history":
        return <RideHistory user={user} />
      default:
        return <Dashboard user={user} onViewChange={setCurrentView} />
    }
  }

  if (!user) {
    return <LoginForm onLogin={handleLogin} />
  }

  return (
    <div className="App">
      <header className="app-header">
        <div className="header-content">
          <h1 className="app-title">CabbyAI</h1>
          <nav className="nav-menu">
            <button
              className={`nav-button ${currentView === "dashboard" ? "active" : ""}`}
              onClick={() => setCurrentView("dashboard")}
            >
              Dashboard
            </button>
            <button
              className={`nav-button ${currentView === "booking" ? "active" : ""}`}
              onClick={() => setCurrentView("booking")}
            >
              Book Ride
            </button>
            <button
              className={`nav-button ${currentView === "history" ? "active" : ""}`}
              onClick={() => setCurrentView("history")}
            >
              Ride History
            </button>
            <button className="logout-button" onClick={handleLogout}>
              Logout
            </button>
          </nav>
        </div>
      </header>
      <main className="main-content">{renderCurrentView()}</main>
    </div>
  )
}

export default App
