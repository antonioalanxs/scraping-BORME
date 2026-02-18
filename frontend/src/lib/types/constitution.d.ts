export interface Constitution {
	id: number;
	entity: {
		code: string;
		name: string;
	};
	address?: string;
	capital?: number;
	date?: string;
	startOfOperations?: string;
	socialObject?: string;
	solePartner?: string;
	appointment?: {
		soleAdministrator?: string;
	};
	singlePersonDeclaration?: boolean;
	registry?: {
		section?: string;
		page?: string;
		entry?: string;
		date?: string;
	};
	source: string;
}
