import axios from 'axios';

import { API_URI } from '$lib/configuration';

const axiosInstance = axios.create({
	baseURL: API_URI
});

axiosInstance.interceptors.response.use(
	(response) => response,
	(error) => Promise.reject(error.response?.data?.detail ?? 'Internal Server Error')
);

export { axiosInstance };
