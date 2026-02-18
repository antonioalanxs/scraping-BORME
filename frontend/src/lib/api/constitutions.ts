import { axiosInstance } from '$lib/utilities/requests/axios';

import type { Constitution } from '$lib/types/constitution';

const PREFIX = '/constitutions';

export async function findConstitutionsByDate(date: string): Promise<Constitution[]> {
	const { data } = await axiosInstance.get<Constitution[]>(`/constitutions?date=${date}`);
	return data;
}

export async function findConstitutionById(id: string | number): Promise<Constitution> {
	const { data } = await axiosInstance.get<Constitution>(`${PREFIX}/${id}`);
	return data;
}
