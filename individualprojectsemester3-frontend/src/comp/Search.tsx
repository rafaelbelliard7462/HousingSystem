import React, { useEffect, useState } from 'react';
import { useDebounce } from "use-debounce";

export default function Search({ applications, setFilteredApplication, setIsDataReady, setSearchQuery, refreshPropertyList } : any) {
   const [inputValue, setInputValue] = useState("");
   const [debouncedValue] = useDebounce(inputValue, 500);

   useEffect(() => {
       if (debouncedValue) {
           // Split the search query into terms
           

           if(!debouncedValue || inputValue === ""){
               setIsDataReady(false);
               console.log("gg")
           }
           else{
               setIsDataReady(true);
           }
           // Update the searched applications
          // setFilteredApplication(filteredApplications);
          setSearchQuery(inputValue)    
          
       }
   }, [debouncedValue, inputValue]);
   

   const handleSearch = (e) => {
    
       setInputValue(e.target.value);
   }

   return (
       <div>
           <label htmlFor="search">Search:</label>
           <input className='mx-2'
               type="text"
               id="search"
               placeholder="Search..."
               onChange={handleSearch}
               style={{backgroundColor : "white", color : "black", border: "2px solid black"}}
           />
       </div>
   );
}
