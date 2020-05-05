//
//  AuthorizeViewController.swift
//  TimeTreeAPI
//
//  Created by EngarNet on 2020/05/04.
//  Copyright © 2020 EngarNet. All rights reserved.
//

import UIKit
import TimeTreeAPICommon
import WebKit
import CryptoKit

public protocol AuthorizeViewControllerDelegate {
    func authorizeCompleted(accessToken: TAccessToken)
}

public class AuthorizeViewController: UIViewController {
    public var params: AuthorizeParams!
    public var delegate: AuthorizeViewControllerDelegate!
    
    public override func viewDidLoad() {
        super.viewDidLoad()
        
        setupView()
    }

    @objc func onCloseClicked(_ sender: UIButton) {
        dismiss(animated: true, completion: nil)
    }
    
    private func setupView() {
        // キャッシュのクリア
        WKWebsiteDataStore.default().removeData(
            ofTypes: WKWebsiteDataStore.allWebsiteDataTypes(),
            modifiedSince: Foundation.Date(timeIntervalSince1970: 0), completionHandler: {})
        
        let navigationBar = UINavigationBar()
        let barItem = UINavigationItem()
        barItem.leftBarButtonItem = UIBarButtonItem(barButtonSystemItem: .cancel, target: self, action: #selector(onCloseClicked(_:)))
        barItem.title = "ログイン"
        navigationBar.items = [barItem]

        let webConfig = WKWebViewConfiguration()
        let webView = WKWebView(frame: .zero, configuration: webConfig)
        webView.navigationDelegate = self
        webView.load(URLRequest(url: URL(string: Config.V1().authorizeUrl + "?" + params.queryParams)!))

        let stackView = UIStackView()
        stackView.frame = view.frame
        stackView.axis = .vertical
        view.addSubview(stackView)

        stackView.addArrangedSubview(navigationBar)
        stackView.addArrangedSubview(webView)
    }

    private func requestToken(code: String) {
        let authorizeApi = AuthorizationApi(apiClient: DefaultApiClient(accessToken: ""))
        let deferred = authorizeApi.token(
            params: self.params.toTokenParam(code: code)
        )
        deferred.invokeOnCompletion { error in
            if let error = error as? HttpRequestException {
                let timetreeError = error.convertDetail()
                print("token error", timetreeError)
            } else {
                let response = deferred.getCompleted() as! TokenResponse
                self.delegate.authorizeCompleted(accessToken: response.toModel())
            }
            DispatchQueue.main.async {
                self.dismiss(animated: true, completion: nil)
            }
        }
    }
}

extension AuthorizeViewController: WKNavigationDelegate {
    public func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if let url = navigationAction.request.url,
            let code = url.queryParam(for: "code"),
            url.absoluteString.hasPrefix(params.redirectUrl) {
            decisionHandler(.cancel)
            requestToken(code: code)
            return
        }
        
        decisionHandler(.allow)
    }
}

public class AuthorizeParams {
    public init(
        clientId: String,
        clientSecret: String,
        redirectUrl: String,
        state: String,
        codeVerifier: String?
    ) {
        self.clientId = clientId
        self.clientSecret = clientSecret
        self.redirectUrl = redirectUrl
        self.state = state
        self.codeVerifier = codeVerifier
    }

    let clientId: String
    let clientSecret: String
    let redirectUrl: String
    let responseType: String = "code"
    let state: String
    let codeVerifier: String?

    var queryParams: String {
        get {
            var params = [
                "client_id=" + clientId,
                "redirect_uri=" + redirectUrl,
                "response_type=" + responseType,
                "state=" + state
            ]
            if let codeVerifier = codeVerifier {
                let data = codeVerifier.data(using: .utf8)!
                let hash = Data(SHA256.hash(data: data))
                let base64 = hash.base64EncodedString().urlSafe
                params.append("code_challenge=" + base64)
                params.append("code_challenge_method=" + "S256")
            }
            return params.joined(separator: "&")
        }
    }
}

extension String {
    var urlSafe: String {
        get {
            replacingOccurrences(of: "+", with: "-")
                .replacingOccurrences(of: "/", with: "_")
                .replacingOccurrences(of: "=", with: "")
        }
    }
}

extension AuthorizeParams {
    func toTokenParam(code: String) -> TokenParams {
        TokenParams(
            clientId: self.clientId,
            clientSecret: self.clientSecret,
            redirectUrl: self.redirectUrl,
            code: code,
            grantType: "authorization_code",
            codeVerifier: self.codeVerifier)
    }
}

extension URL {
    func queryParam(for key: String) -> String? {
        let queryItems = URLComponents(string: absoluteString)?.queryItems
        return queryItems?.filter { $0.name == key }.compactMap { $0.value }.first
    }
}
