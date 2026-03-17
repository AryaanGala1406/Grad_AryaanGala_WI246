import React from 'react'

function Login() {

  function handleLogin(e) {
    e.preventDefault()
    alert("Login Successful")
  }

  return (

    <div>

      <form onSubmit={handleLogin}>

        <h3>Login</h3>

        <div className="form-group">
          <label>Username</label>
          <input type="text" required />
        </div>

        <div className="form-group">
          <label>Password</label>
          <input type="password" required />
        </div>

        <button type="submit">Login</button>

      </form>
    </div>

  )
}

export default Login