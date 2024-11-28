import { Vendor } from "./vendor.model";

export interface Event{
    eventId: number;
    eventName: string;
    eventLocation: string;
    totalTickets: number;
    ticketPrice: number;
    eventStatus: boolean;
    vendor: Vendor;
}

export interface EventDTO{
    eventName: string;
    eventLocation: string;
    totalTickets: number;
    eventStatus: boolean;
    ticketPrice: number;
    vendor: Vendor;
}