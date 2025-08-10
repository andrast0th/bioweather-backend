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
}
