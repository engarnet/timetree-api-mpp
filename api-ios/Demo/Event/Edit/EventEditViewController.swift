//
//  EventEditViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/06.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPI

class EventEditViewController: UIViewController {
    @IBOutlet weak var titleTextField: UITextField!
    @IBOutlet weak var DescriptionTextField: UITextField!
    @IBOutlet weak var keepSwitch: UISwitch!
    @IBOutlet weak var allDaySwitch: UISwitch!
    @IBOutlet weak var startDatePicker: UIDatePicker!
    @IBOutlet weak var endDatePicker: UIDatePicker!
    @IBOutlet weak var urlTextField: UITextField!
    @IBOutlet weak var locationTextField: UITextField!
    
    var calendar: TCalendar!
    var event: TEvent?
    
    var labels: [TLabel] = []
    var members: [TUser] = []
    
    private var isUpdate: Bool {
        get {
            event != nil
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadData()
        setup()
    }

    private func loadData() {
        let client = Container.timeTreeClient!
        client.calendarMembers(calendarId: calendar.id) { result in
            switch result {
            case .success(let users):
                self.members = users
            case .failure(let error):
                print(error)
            }
        }
        client.calendarLabels(calendarId: calendar.id) { result in
            switch result {
            case .success(let labels):
                self.labels = labels
            case .failure(let error):
                print(error)
            }
        }
    }

    private func setup() {
        self.navigationItem.rightBarButtonItem = UIBarButtonItem(barButtonSystemItem: .save, target: self, action: #selector(onSaveTapped(_:)))
        
        if isUpdate {
            self.title = "Update Data"
            self.titleTextField.text = event?.title
            self.DescriptionTextField.text = event?.description
            self.startDatePicker.date = event?.startAt ?? Date()
            self.endDatePicker.date = event?.endAt ?? Date()
            self.urlTextField.text = event?.url?.absoluteString
            self.locationTextField.text = event?.location
        } else {
            self.title = "New Data"
        }
    }

    @objc func onSaveTapped(_ sender: UIView) {
        if keepSwitch.isOn {
            saveKeep()
        } else {
            saveSchedule()
        }
    }
    
    private func saveKeep() {
        let client = Container.timeTreeClient!
        if isUpdate {
            let event = self.event!
            client.updateKeep(
                calendarId: calendar.id,
                eventId: event.id,
                title: titleTextField.text ?? "NewKeep",
                description: DescriptionTextField.text,
                location: locationTextField.text,
                url: URL(string: urlTextField.text ?? ""),
                label: labels[0],
                attendees: members
            ) { result in
                self.handleResult(result)
            }
        } else {
            client.addKeep(
                calendarId: calendar.id,
                title: titleTextField.text ?? "NewKeep",
                description: DescriptionTextField.text,
                location: locationTextField.text,
                url: URL(string: urlTextField.text ?? ""),
                label: labels[0],
                attendees: members
            ) { result in
                self.handleResult(result)
            }
        }
    }
    
    private func saveSchedule() {
        let client = Container.timeTreeClient!
        if isUpdate {
            let event = self.event!
            client.updateSchedule(
                calendarId: calendar.id,
                eventId: event.id,
                title: titleTextField.text ?? "NewSchedule",
                allDay: allDaySwitch.isOn,
                startAt: startDatePicker.date,
                endAt: endDatePicker.date,
                description: DescriptionTextField.text,
                location: locationTextField.text,
                url: URL(string: urlTextField.text ?? ""),
                label: labels[0],
                attendees: members
            ) { result in
                self.handleResult(result)
            }
        } else {
            client.addSchedule(
                calendarId: calendar.id,
                title: titleTextField.text ?? "NewEvent",
                allDay: allDaySwitch.isOn,
                startAt: startDatePicker.date,
                endAt: endDatePicker.date,
                description: DescriptionTextField.text,
                location: locationTextField.text,
                url: URL(string: urlTextField.text ?? ""),
                label: labels[0],
                attendees: members
            ) { result in
                self.handleResult(result)
            }
        }
    }
    
    @IBAction func onKeepValueChanged(_ sender: Any) {
        if keepSwitch.isOn {
            allDaySwitch.isEnabled = false
            startDatePicker.isEnabled = false
            endDatePicker.isEnabled = false
        } else {
            allDaySwitch.isEnabled = true
            startDatePicker.isEnabled = true
            endDatePicker.isEnabled = true
        }
    }
    
    @IBAction func onAllDayValueChanged(_ sender: Any) {
        if allDaySwitch.isOn {
            startDatePicker.datePickerMode = .date
            endDatePicker.datePickerMode = .date
        } else {
            startDatePicker.datePickerMode = .dateAndTime
            endDatePicker.datePickerMode = .dateAndTime
        }
    }

    private func handleResult(_ result: Result<TEvent, TimeTreeError>) {
        switch result {
        case .success(_):
            DispatchQueue.main.async {
                let nc = self.navigationController!
                let vc = nc.findViewController(ofClass: EventListViewController.self) as? EventListViewController
                vc?.fetchEvents()
                nc.popToViewController(ofClass: EventListViewController.self)
            }
        case .failure(let error):
            print(error)
        }
    }
}

extension EventEditViewController {
    static func instance(calendar: TCalendar, event: TEvent? = nil) -> EventEditViewController {
        let sb = UIStoryboard(name: "EventEdit", bundle: nil)
        let vc = sb.instantiateInitialViewController() as! EventEditViewController
        vc.calendar = calendar
        vc.event = event
        return vc
    }
}
