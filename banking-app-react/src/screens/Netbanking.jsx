import React, { useState } from 'react'
import Login from './Login'
import Signup from './Signup'

function Netbanking() {

  const [mode, setMode] = useState("login")

  return (
    <div className="container">

      <h2>Net Banking</h2>

      {/* Toggle Menu */}

      <div className="menu">

        <span
          style={{cursor:"pointer", marginRight:"20px"}}
          onClick={() => setMode("login")}
        >
          Login
        </span>

        |

        <span
          style={{cursor:"pointer", marginLeft:"20px"}}
          onClick={() => setMode("signup")}
        >
          Signup
        </span>

      </div>

      <hr/>

      {/* Component Toggle */}

      {mode === "login" && <Login />}

      {mode === "signup" && <Signup />}

    </div>
  )
}

export default Netbanking