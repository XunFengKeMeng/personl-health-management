import axios from "axios"
const URL_API = 'http://localhost:21090/api/health-management'
const request = axios.create({
  baseURL: URL_API,
  timeout: 8000
});
export default request;