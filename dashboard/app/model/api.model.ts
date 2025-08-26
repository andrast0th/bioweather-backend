export type NotificationType =
  | 'bw'
  | 'bedtime'
  | 'wakeup'
  | 'last-meal'
  | 'next-rest'
  | 'peak'
  | 'exercise';

export interface Subscription {
  pushToken: string;
  userId: string;
  deviceInfo: string;
  language: string;
  timezoneOffset: number;
  locationId: string;
  locationName: string;
  notificationTypes: NotificationType[];
}

export interface DeviceInfo {
  brand: string;
  deviceName: string;
  isDevice: string;
  manufacturer: string;
  modelName: string;
  osName: string;
  osVersion: string;
}

export interface Device {
  pushToken: string;
  userId: string;
  deviceInfo: DeviceInfo;
  language: string;
  timezoneOffset: number;
  updatedTimestamp: string; // ISO 8601 date string
  selectedBwConditions: string[];
}

export interface Language {
  code: string;
  name: string;
}

export interface Translation {
  id: string;
  text: string;
}

export interface CacheStats {
  requestCount: number;
  hitCount: number;
  missCount: number;
  loadSuccessCount: number;
  loadFailureCount: number;
  totalLoadTime: number;
  evictionCount: number;
  evictionWeight: number;
}

export interface CacheStatistics {
  [cacheName: string]: CacheStats;
}

export interface PushTicket {
  id: string;
  pushToken: string;
  receiptStatus: string;
  notificationType: NotificationType;
  notificationTitle: string;
  notificationBody: string;
  locationId: string;
  receiptError: string;
  wasReceiptChecked: boolean;
  receiptCheckedAt: string; // ISO date string, use Date if you want JS Date object
  ticketCreatedAt: string; // ISO date string, use Date if you want JS Date object
}

export interface Config {
  notificationJobCron: string;
  notificationThresholdMinutes: number;
  bwTodayNotificationHour: string;
  bwTomorrowNotificationHour: string;
}
