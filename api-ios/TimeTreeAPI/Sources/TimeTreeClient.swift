//
//  TimeTreeClient.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/01.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import Foundation
import TimeTreeAPICommon

public class TimeTreeClient {
    private let apiClient: ApiClient!

    public init(accessToken: String) {
        apiClient = DefaultApiClient.init(accessToken: accessToken)
    }
}

extension HttpRequestException {
    func convertDetail() -> TimeTreeError {
        let responseCode = Int(self.responseCode)
        let error = self.error.toModel(responseCode: responseCode)
        switch responseCode {
        case 400: return TimeTreeError.BadRequestError(error)
        case 401: return TimeTreeError.UnauthorizedError(error)
        case 403: return TimeTreeError.ForbiddenError(error)
        case 404: return TimeTreeError.NotFoundError(error)
        case 406: return TimeTreeError.NotAcceptableError(error)
        case 429: return TimeTreeError.TooManyRequestError(error)
        case 500: return TimeTreeError.InternalServerErrorError(error)
        case 503: return TimeTreeError.ServiceUnavailableError(error)
        case 504: return TimeTreeError.TimeoutError(error)
        default: return TimeTreeError.UnknownError("unknown error")
        }
    }
}

extension TimeTreeClient {
    // UserAPI
    public func user(completion: @escaping (Result<TUser, TimeTreeError>) -> ()) {
        let deferred = UserApi(apiClient: apiClient).user()
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! UserResponse
                completion(.success(response.toModel()))
            }
        }
    }
}

extension TimeTreeClient {
    // CalendarAPI
    public func calendars(completion: @escaping (Result<[TCalendar], TimeTreeError>) -> ()) {
        let deferred = CalendarApi(apiClient: apiClient)
            .calendars(
                params: CalendarsParams(
                    include: Include.Calendars(
                        labels: true,
                        members: true
                )
            )
        )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! CalendarsResponse
                completion(.success(response.toModel()))
            }
        }
    }
    
    public func calendar(calendarId: String, completion: @escaping (Result<TCalendar, TimeTreeError>) -> ()) {
        let deferred = CalendarApi(apiClient: apiClient)
            .calendar(
                params: CalendarParams(
                    calendarId: calendarId,
                    include: Include.Calendars(
                        labels: true,
                        members: true
                    )
                )
        )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! CalendarResponse
                completion(.success(response.toModel()))
            }
        }
    }
    
    public func calendarLabels(calendarId: String, completion: @escaping (Result<[TLabel], TimeTreeError>) -> ()) {
        let deferred = CalendarApi(apiClient: apiClient)
            .calendarLabels(
                params: CalendarLabelsParams(
                    calendarId: calendarId
                )
        )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! CalendarLabelsResponse
                completion(.success(response.toModel()))
            }
        }
    }
    
    public func calendarMembers(calendarId: String, completion: @escaping (Result<[TUser], TimeTreeError>) -> ()) {
        let deferred = CalendarApi(apiClient: apiClient)
            .calendarMembers(
                params: CalendarMembersParams(
                    calendarId: calendarId
                )
        )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! CalendarMembersResponse
                completion(.success(response.toModel()))
            }
        }
    }
}

extension TimeTreeClient {
    // EventAPI
    public func event(calendarId: String, eventId: String, completion: @escaping (Result<TEvent, TimeTreeError>) -> ()) {
        let deferred = EventsApi(apiClient: apiClient)
            .event(
                params: EventParams(
                    calendarId: calendarId,
                    eventId: eventId,
                    include: Include.Events(label: true, creator: true, attendees: true)
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! EventResponse
                completion(.success(response.toModel()))
            }
        }
    }

    public func upcomingEvents(calendarId: String, timeZone: TimeZone, completion: @escaping (Result<[TEvent], TimeTreeError>) -> ()) {
        let deferred = EventsApi(apiClient: apiClient)
            .upcomingEvents(
                params: UpcomingEventsParams(
                    calendarId: calendarId,
                    timeZone: timeZone.identifier,
                    days: 7,
                    include: Include.Events(label: true, creator: true, attendees: true)
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! UpcomingEventsResponse
                completion(.success(response.toModel()))
            }
        }
    }

    public func addSchedule(
        calendarId: String,
        title: String,
        allDay: Bool,
        startAt: Foundation.Date,
        endAt: Foundation.Date,
        timeZone: TimeZone? = nil,
        description: String?,
        location: String?,
        url: URL?,
        label: TLabel,
        attendees: [TUser]?,
        completion: @escaping (Result<TEvent, TimeTreeError>) -> ()
    ) {
        let deferred = EventsApi(apiClient: apiClient)
            .addEvent(
                params: AddEventsParams.AddScheduleParams(
                    calendarId: calendarId,
                    title: title,
                    allDay: allDay,
                    startAt: TimeTreeAPICommon.Date(time: Int64(startAt.timeIntervalSince1970) * 1000), // msを渡す必要があるので1000倍している
                    startTimeZone: timeZone?.identifier,
                    endAt: TimeTreeAPICommon.Date(time: Int64(endAt.timeIntervalSince1970) * 1000), // msを渡す必要があるので1000倍している
                    endTimeZone: timeZone?.identifier,
                    description: description,
                    location: location,
                    url: url?.absoluteString,
                    labelId: label.id,
                    attendees: attendees?.map { $0.id }
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! EventResponse
                self.event(calendarId: calendarId, eventId: response.data.id) {
                    completion($0)
                }
            }
        }
    }

    public func addKeep(
        calendarId: String,
        title: String,
        description: String?,
        location: String?,
        url: URL?,
        label: TLabel,
        attendees: [TUser]?,
        completion: @escaping (Result<TEvent, TimeTreeError>) -> ()
    ) {
        let deferred = EventsApi(apiClient: apiClient)
            .addEvent(
                params: AddEventsParams.AddKeepParams(
                    calendarId: calendarId,
                    title: title,
                    allDay: false, // TODO: Keepの場合allDayはOptionalだがnullを渡すと400エラーになるので設定
                    startAt: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                    startTimeZone: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                    endAt: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                    endTimeZone: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                    description: description,
                    location: location,
                    url: url?.absoluteString,
                    labelId: label.id,
                    attendees: attendees?.map { $0.id }
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! EventResponse
                self.event(calendarId: calendarId, eventId: response.data.id) {
                    completion($0)
                }
            }
        }
    }
    
    public func updateSchedule(
        calendarId: String,
        eventId: String,
        title: String,
        allDay: Bool,
        startAt: Foundation.Date,
        endAt: Foundation.Date,
        timeZone: TimeZone? = nil,
        description: String?,
        location: String?,
        url: URL?,
        label: TLabel,
        attendees: [TUser]?,
        completion: @escaping (Result<TEvent, TimeTreeError>) -> ()
    ) {
        let deferred = EventsApi(apiClient: apiClient)
            .updateEvent(
                params: UpdateEventParams(
                    eventId: eventId,
                    addEventsParams: AddEventsParams.AddScheduleParams(
                        calendarId: calendarId,
                        title: title,
                        allDay: allDay,
                        startAt: TimeTreeAPICommon.Date(time: Int64(startAt.timeIntervalSince1970) * 1000), // msを渡す必要があるので1000倍している
                        startTimeZone: timeZone?.identifier,
                        endAt: TimeTreeAPICommon.Date(time: Int64(endAt.timeIntervalSince1970) * 1000), // msを渡す必要があるので1000倍している
                        endTimeZone: timeZone?.identifier,
                        description: description,
                        location: location,
                        url: url?.absoluteString,
                        labelId: label.id,
                        attendees: attendees?.map { $0.id }
                    )
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! EventResponse
                self.event(calendarId: calendarId, eventId: response.data.id) {
                    completion($0)
                }
            }
        }
    }

    public func updateKeep(
        calendarId: String,
        eventId: String,
        title: String,
        description: String?,
        location: String?,
        url: URL?,
        label: TLabel,
        attendees: [TUser]?,
        completion: @escaping (Result<TEvent, TimeTreeError>) -> ()
    ) {
        let deferred = EventsApi(apiClient: apiClient)
            .updateEvent(
                params: UpdateEventParams(
                    eventId: eventId,
                    addEventsParams: AddEventsParams.AddKeepParams(
                        calendarId: calendarId,
                        title: title,
                        allDay: false, // TODO: Keepの場合allDayはOptionalだがnullを渡すと400エラーになるので設定
                        startAt: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                        startTimeZone: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                        endAt: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                        endTimeZone: nil, // TODO: 入力不要なのでできれば指定なしにしたい
                        description: description,
                        location: location,
                        url: url?.absoluteString,
                        labelId: label.id,
                        attendees: attendees?.map { $0.id }
                    )
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! EventResponse
                self.event(calendarId: calendarId, eventId: response.data.id) {
                    completion($0)
                }
            }
        }
    }

    public func deleteEvent(calendarId: String, eventId: String, completion: @escaping (Result<Void, TimeTreeError>) -> ()) {
        let deferred = EventsApi(apiClient: apiClient)
            .deleteEvent(
                params: DeleteEventsParams(
                    calendarId: calendarId,
                    eventId: eventId
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                completion(.success(()))
            }
        }
    }
}

extension TimeTreeClient {
    // ActivityApi
    public func addComment(calendarId: String, eventId: String, comment: String, completion: @escaping (Result<TComment, TimeTreeError>) -> ()) {
        let deferred = ActivityApi(apiClient: apiClient)
            .addActivity(
                params: AddActivityParams(
                    calendarId: calendarId,
                    eventId: eventId,
                    content: comment
                )
            )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                completion(.failure(error.convertDetail()))
            } else {
                let response = deferred.getCompleted() as! ActivityResponse
                completion(.success(response.toModel()))
            }
        }
    }
}
