import axios from "axios";
import TokenManager from "./TokenManager";

axios.defaults.baseURL = "http://localhost:8080";
const SearchAndFilterApi = {

  getAplicationsPageByHomeowner: (
    userId: number,
    currentPage: number,
    itemsPerPage: number,
    status: string
  ) =>
    axios
      .get(
        `/searchAndFilter/findApplicationsByHomeowner/${userId}?status=${status}&pageNum=${currentPage}&itemsNum=${itemsPerPage}`,
        {
          headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
        }
      )
      .then((response) => response.data),
      searchAplicationsPageByHomeowner: (
        userId: number,
        currentPage: number,
        itemsPerPage: number,
        status: string,
        searchString : string
      ) =>
        axios
          .get(
            `/searchAndFilter/search/${userId}?status=${status}&searchString=${searchString}&pageNum=${currentPage}&itemsNum=${itemsPerPage}`,
            {
              headers: { Authorization: `Bearer ${TokenManager.getAccessToken()}` },
            }
          )
          .then((response) => response.data),
};


export default SearchAndFilterApi;
