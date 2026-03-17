import React, { useState } from 'react'

function Signup() {

  const [message,setMessage] = useState("")

  function register(e){
    e.preventDefault()
    setMessage("Account created successfully!")
  }

  return (

    <form onSubmit={register}>

      <h3>Create Account</h3>

      <div className="form-group">
        <label>Name</label>
        <input type="text" required />
      </div>

      <div className="form-group">
        <label>Email</label>
        <input type="email" required />
      </div>

      <div className="form-group">
        <label>Password</label>
        <input type="password" required />
      </div>

      <button type="submit">Signup</button>

      <p className="result">{message}</p>

    </form>

  )
}

export default Signup