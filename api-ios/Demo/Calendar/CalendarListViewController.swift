//
//  CalendarListViewController.swift
//  Demo
//
//  Created by EngarNet on 2020/05/06.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPI

class CalendarListViewController: UITableViewController {
    private var calendars: [TCalendar] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = "Calendar List"
        
        Container.timeTreeClient.calendars { result in
            switch result {
            case .success(let calendars):
                self.reloadData(calendars: calendars)
            case .failure(let error):
                print(error)
            }
        }
    }

    private func reloadData(calendars: [TCalendar]) {
        self.calendars = calendars
        tableView.reloadData()
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        calendars.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "CalendarListCell") as? CalendarListCell else {
            return UITableViewCell()
        }
        cell.colorView.backgroundColor = calendars[indexPath.row].color
        cell.nameLabel.text = calendars[indexPath.row].name
        cell.descriptionLabel.text = calendars[indexPath.row].description
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        moveEventList(calendar: self.calendars[indexPath.row])
    }

    private func moveEventList(calendar: TCalendar) {
        let vc = EventListViewController.instance(calendar: calendar)
        self.navigationController?.pushViewController(vc, animated: true)
    }
}

extension CalendarListViewController {
    static var instance: CalendarListViewController {
        get {
            let sb = UIStoryboard(name: "CalendarList", bundle: nil)
            return sb.instantiateInitialViewController() as! CalendarListViewController
        }
    }
}

class CalendarListCell: UITableViewCell {
    @IBOutlet weak var colorView: UIView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var descriptionLabel: UILabel!
}
