//
//  ViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/01.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPI

class ViewController: UIViewController {

    // MARK: oAuth app settings
    // https://timetreeapp.com/oauth/applications
    private let authorizeParams = AuthorizeParams(
        clientId: "",
        clientSecret: "",
        redirectUrl: "",
        state: UUID().uuidString,
        codeVerifier: UUID().uuidString
    )
    
    // MARK: personal access token setting
    // https://timetreeapp.com/personal_access_tokens
    private let personalAccessToken = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.title = "TimeTreeAPIDemo"
        // Do any additional setup after loading the view.

        let client = TimeTreeClient(accessToken: "")
        client.user { result in
            switch result {
            case .success(let user):
                print(user.name)
            case .failure(let error):
                print(error)
            }
        }

        client.calendars { result in
            switch result {
            case .success(let calendars):
                calendars.forEach {
                    print($0.name)
                    client.upcomingEvents(calendarId: $0.id, timeZone: TimeZone.current) { events in
                        switch events {
                        case .success(let events):
                            events.forEach { print($0.title) }
                        case .failure(let error):
                            print(error)
                        }
                    }
                }
            case .failure(let error):
                print(error)
            }
        }
        
        client.calendarLabels(calendarId: "hGL8r83T18UE") { result in
            switch result {
            case .success(let labels):
                client.addSchedule(
                    calendarId: "hGL8r83T18UE",
                    title: "ios_sche",
                    allDay: false,
                    startAt: Date(),
                    endAt: Date(),
                    description: "説明",
                    location: "大阪",
                    url: URL(string: "http://www.yahoo.co.jp"),
                    label: labels[0],
                    attendees: nil
                ) {result in
                    switch result {
                    case .success(let event):
                        print(event.id)
                    case .failure(let error):
                        print(error)
                    }
                }
            case .failure(let error):
                print(error)
            }
        }
    }
    
    @IBAction func onSignInTapped(_ sender: Any) {
        let vc = AuthorizeViewController()
        vc.delegate = self
        vc.params = authorizeParams
        self.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func onPersonalAccessTokenTapped(_ sender: Any) {
        Container.timeTreeClient = TimeTreeClient(accessToken: self.personalAccessToken)
        moveNext()
    }

    private func moveNext() {
        let vc = CalendarListViewController.instance
        self.navigationController?.pushViewController(vc, animated: true)
    }
}

extension ViewController: AuthorizeViewControllerDelegate {
    func authorizeCompleted(accessToken: TAccessToken) {
        DispatchQueue.main.async {
            Container.timeTreeClient = TimeTreeClient(accessToken: accessToken.accessToken)
            self.moveNext()
        }
    }
}
