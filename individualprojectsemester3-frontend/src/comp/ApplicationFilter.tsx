import React from 'react'

export default function ApplicationFilter({setStatus} : any) {

    const handleChange = (e : any) =>{
        e.preventDefault();

        setStatus(e.target.value);
    }
  return (
    <div>
        <label htmlFor="position">Filter:</label>
        <select className='mx-2'
        id="position"
        onChange={handleChange }
        style={{backgroundColor : "white", color: "black"}}
        >
        <option value="PENDING">Pending</option>
        <option value="ACCEPTED">Accepted</option>
        <option value="DECLINED">Declined</option>
        </select>
    </div>
  )
}
