import React, { useState, useEffect } from 'react';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { Bar } from 'react-chartjs-2';
import { CategoryScale } from 'chart.js';
import Chart from 'chart.js/auto';
import LoginApi from '../api/LoginApi';
import TokenManager from '../api/TokenManager';
import { useNavigate } from 'react-router-dom';

Chart.register(CategoryScale);

const BarChart = () => {
 const [selectedYear, setSelectedYear] = useState(new Date("2024"));
 const [data, setData] = useState(null);
 const navigate = useNavigate();
 const handleYearChange = (date) => {
  setSelectedYear(new Date(date.getFullYear(), 0, 1));
 };
 

 useEffect(() => {
 axios.get(`http://localhost:8080/propertyStatistics/${selectedYear.getFullYear()}`, {
 headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` }
})
 .then((response) => {
   const stats = response.data;
   console.log(stats, 'g')
   const labels = Array.from({ length: 12 }, (_, i) => new Date(0, i).toLocaleString('default', { month: 'long' }));
   const dataValues = labels.map(month => {
     const stat = stats.find(stat => new Date(0, stat.month - 1).toLocaleString('default', { month: 'long' }) === month);
     return stat ? stat.totalMonthlyPrice : 0;
   });
   setData({
     labels: labels,
     datasets: [{
       label: 'Total revenue',
       backgroundColor: 'rgba(75,192,192,0.2)',
       borderColor: 'rgba(75,192,192,1)',
       borderWidth: 1,
       hoverBackgroundColor: 'rgba(75,192,192,0.4)',
       hoverBorderColor: 'rgba(75,192,192,1)',
       data: dataValues,
       barPercentage: 1,
       categoryPercentage: 1,
     }],
   });
 })
 .catch((error) => {
   if (TokenManager.isAccessTokenExpired()) {
     LoginApi.renew().catch((error) => {
       if (error.message === "Request failed with status code 401") {
         navigate("/loginpage", { state: { from: window.location.pathname } });
       } else {
         console.error(error.message);
       }
     });
   } else {
     console.error( error);
   }
 });
 }, [selectedYear]);

 const options = {
 scales: {
 y: {
   min: 0,
   max: 2500,
   beginAtZero: true,
   ticks: {
     callback: function(value, index, values) {
       return '€' + value;
     }
   }
 },
 }
 };

 return (
 <div>
 <DatePicker
   selected={selectedYear}
   onChange={handleYearChange}
   dateFormat="yyyy"
   showYearPicker
   minDate={new Date('2024')}
   scrollableYearDropdown
   yearDropdownItemNumber={20}
 />
 {data && <Bar data={data} options={options} width={800} height={400} />}
 </div>
 );
};

export default BarChart;
