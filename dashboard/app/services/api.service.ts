import axios from 'axios';
import type { ActuatorInfo } from '~/model/actuator.model';
import type { CacheStatistics, Device, PushTicket, Subscription } from '~/model/api.model';

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

const requestInterceptor = (config: any) => {
  config.headers.Authorization = getAuthHeader();
  config.headers['X-Requested-With'] = 'XMLHttpRequest';
  return config;
};

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
});
export const actuatorClient = axios.create({
  baseURL: '/actuator/',
});

apiClient.interceptors.request.use(requestInterceptor);
actuatorClient.interceptors.request.use(requestInterceptor);

const responseInterceptor = (error: any) => {
  if (
    (error.response?.status === 401 || error.response?.status === 403) &&
    typeof window !== 'undefined' &&
    window.location.pathname !== '/login'
  ) {
    window.location.href = '/login';
  }
  return Promise.reject(error);
};
apiClient.interceptors.response.use((response) => response, responseInterceptor);
actuatorClient.interceptors.response.use((response) => response, responseInterceptor);

export const getActuatorInfo = async (): Promise<ActuatorInfo> => {
  const response = await actuatorClient.get<ActuatorInfo>('/info');
  return response.data;
};

export const getCacheStatistics = async (): Promise<CacheStatistics> => {
  const response = await apiClient.get<CacheStatistics>('/cache');
  return response.data;
};

export const fetchSubscriptions = async (pushToken: string): Promise<Subscription[]> => {
  const response = await apiClient.get<Subscription[]>(
    `/notifications/subscription/${encodeURIComponent(pushToken)}`
  );
  return response.data;
};

export const fetchDevices = async (query?: string): Promise<Device[]> => {
  const params = query ? { params: { query } } : {};
  const response = await apiClient.get<Device[]>('/devices', params);
  return response.data;
};

export const fetchNotificationHistory = async (
  pushToken: string,
  locationId?: string | null
): Promise<PushTicket[]> => {
  const params = locationId ? { params: { locationId } } : {};

  const response = await apiClient.get<PushTicket[]>(
    `/notifications/history/${encodeURIComponent(pushToken)}`,
    params
  );

  return response.data;
};

export const sendTestNotification = async (pushToken: string, message: string): Promise<void> => {
  await apiClient.post<void>('/notifications/test-notification', {
    pushToken,
    message,
  });
};
