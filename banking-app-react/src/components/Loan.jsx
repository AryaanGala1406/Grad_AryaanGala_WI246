import React, { useState } from 'react'

function Loan() {

  const [applicant, setApplicant] = useState("")
  const [interest, setInterest] = useState("")
  const [result, setResult] = useState("")
  const [error, setError] = useState("")

  const [amount, setAmount] = useState("")
  const [years, setYears] = useState("")
  const [type, setType] = useState("")

  function changeLoan(e) {
    const val = e.target.value
    setType(val)

    if (val === "HOME") setInterest(9)
    else if (val === "CAR") setInterest(12)
    else if (val === "PERSONAL") setInterest(15)
    else setInterest("")
  }

  function calculate() {

    setError("")
    setResult("")

    let name = applicant.trim()
    let amt = parseFloat(amount)
    let y = parseInt(years)
    let rate = parseFloat(interest)

    // -------- BASIC VALIDATIONS --------

    if (name === "") {
      setError("Applicant name is required")
      return
    }

    if (name.length < 5) {
      setError("Applicant name must be at least 5 characters")
      return
    }

    if (!/^[A-Za-z ]+$/.test(name)) {
      setError("Name must contain only letters")
      return
    }

    if (type === "") {
      setError("Please select loan type")
      return
    }

    if (isNaN(amt) || amt <= 0) {
      setError("Enter valid loan amount")
      return
    }

    if (isNaN(y) || y <= 0) {
      setError("Enter valid duration")
      return
    }

    // -------- TYPE BASED VALIDATIONS --------

    if (type === "HOME") {
      if (amt < 500000) {
        setError("Home loan amount must be more than 5 Lakhs")
        return
      }
      if (y > 30) {
        setError("Home loan duration must be less than 30 years")
        return
      }
    }

    if (type === "CAR") {
      if (amt < 100000) {
        setError("Car loan amount must be more than 1 Lakh")
        return
      }
      if (y > 7) {
        setError("Car loan duration must be less than 7 years")
        return
      }
    }

    if (type === "PERSONAL") {
      if (amt < 10000) {
        setError("Personal loan amount must be more than 10,000")
        return
      }
      if (y > 5) {
        setError("Personal loan duration must be less than 5 years")
        return
      }
    }

    // -------- EMI CALCULATION --------

    let monthlyRate = rate / 12 / 100
    let months = y * 12

    let emi =
      (amt * monthlyRate * Math.pow(1 + monthlyRate, months)) /
      (Math.pow(1 + monthlyRate, months) - 1)

    setResult(`EMI for ${name} is ₹${emi.toFixed(2)}`)
  }

  return (
    <div className="container">

      <h2>Loan Calculator</h2>

      <div className="form-group">
        <label>Applicant</label>
        <input
          type="text"
          value={applicant}
          onChange={(e) => setApplicant(e.target.value)}
        />
      </div>

      <div className="form-group">
        <label>Loan Type</label>
        <select onChange={changeLoan}>
          <option value="">Select</option>
          <option value="HOME">Home</option>
          <option value="CAR">Car</option>
          <option value="PERSONAL">Personal</option>
        </select>
      </div>

      <div className="form-group">
        <label>Interest</label>
        <input value={interest} readOnly />
      </div>

      <div className="form-group">
        <label>Loan Amount</label>
        <input
          type="number"
          onChange={(e) => setAmount(e.target.value)}
        />
      </div>

      <div className="form-group">
        <label>Duration (Years)</label>
        <input
          type="number"
          onChange={(e) => setYears(e.target.value)}
        />
      </div>

      <button onClick={calculate}>Calculate EMI</button>

      <p className="error">{error}</p>
      <p className="result">{result}</p>

    </div>
  )
}

export default Loan