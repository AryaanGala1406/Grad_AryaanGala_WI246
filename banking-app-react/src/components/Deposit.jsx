import React,{useState} from 'react'

function Deposit(){

  const [amount,setAmount] = useState("")
  const [tenure,setTenure] = useState("")
  const [result,setResult] = useState("")

  const rate = 7

  function calculate(){

    let p = parseFloat(amount)
    let t = parseFloat(tenure)

    let maturity = p * Math.pow((1 + rate/100), t)

    setResult("Maturity Amount: ₹" + maturity.toFixed(2))
  }

  return(

    <div className="container">

      <h2>Deposit Calculator</h2>

      <div className="form-group">
        <label>Deposit Amount</label>
        <input type="number" onChange={(e)=>setAmount(e.target.value)}/>
      </div>

      <div className="form-group">
        <label>Tenure (Years)</label>
        <input type="number" onChange={(e)=>setTenure(e.target.value)}/>
      </div>

      <div className="form-group">
        <label>Interest Rate</label>
        <input value="7%" readOnly/>
      </div>

      <button onClick={calculate}>Calculate</button>

      <p className="result">{result}</p>

    </div>

  )
}

export default Deposit