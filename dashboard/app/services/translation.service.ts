import type { Language, Translation } from '~/model/api.model';
import { apiClient } from '~/services/api.service';

export const getLanguages = async (): Promise<Language[]> => {
  const response = await apiClient.get<Language[]>('/translations');
  return response.data;
};

export const getTranslations = async (lang: String): Promise<Translation[]> => {
  const response = await apiClient.get<Translation[]>(`/translations/${lang}`);
  return response.data;
};

export const deleteTranslations = async (lang: String): Promise<void> => {
  await apiClient.delete<void>(`/admin/translations/${lang}`);
};

export const importTranslations = async (lang: String, payload: any): Promise<void> => {
  await apiClient.post<void>(`/admin/translations/${lang}/import`, payload);
};
