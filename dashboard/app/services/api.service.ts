import axios from 'axios';
import type { ActuatorInfo } from '~/model/actuator.model';

export const API_BASE_URL = '/api/v1/';

export const getAuthHeader = (): string | null => {
  if (typeof window === 'undefined') {
    return null;
  }
  return localStorage?.getItem('authHeader');
};

export const setAuth = (username: string, password: string): void => {
  if (typeof window === 'undefined') {
    return;
  }
  localStorage.setItem('authHeader', `Basic ${btoa(`${username}:${password}`)}`);
};

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
});
apiClient.interceptors.request.use((config) => {
  config.headers.Authorization = getAuthHeader();
  config.headers['X-Requested-With'] = 'XMLHttpRequest';
  return config;
});

export const actuatorClient = axios.create({
  baseURL: '/actuator/',
});
actuatorClient.interceptors.request.use((config) => {
  config.headers.Authorization = getAuthHeader();
  config.headers['X-Requested-With'] = 'XMLHttpRequest';
  return config;
});

export const getActuatorInfo = async (): Promise<ActuatorInfo> => {
  const response = await actuatorClient.get<ActuatorInfo>('/info');
  return response.data;
};
