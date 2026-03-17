import React, { useState } from 'react'
import Loan from '../components/Loan'
import Deposit from '../components/Deposit'

function Services() {

  const [service, setService] = useState("loan")

  return (
    <div className="service-menu">

      <h2>Banking Services</h2>

      {/* Toggle Menu */}
      <div className="menu">
        <span
          style={{cursor:"pointer", marginRight:"20px"}}
          onClick={() => setService("loan")}
        >
          Loan Calculator
        </span>

        |

        <span
          style={{cursor:"pointer", marginLeft:"20px"}}
          onClick={() => setService("deposit")}
        >
          Deposit Calculator
        </span>
      </div>

      <hr/>

      {/* Conditional Rendering */}

      {service === "loan" && <Loan/>}

      {service === "deposit" && <Deposit/>}

    </div>
  )
}

export default Services