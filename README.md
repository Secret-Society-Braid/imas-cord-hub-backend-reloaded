APIの利活用に関するドキュメントは [こちら](https://Secret-Society-Braid/imas-cord-hub-backend-reloaded/wiki) です。このREADMEはAPIの開発に関する説明書きのドキュメントです。

## 概要
このリポジトリでは、プロジェクト「ImasCordHub」でサーバー並びにファンサイト情報を提供するAPIを開発しています。

また、外部のユーザーさんが利活用できるようAPIに外部公開も行っています。

## ビルド

**API自体の利用方法（エンドポイント情報など）は [こちら](https://Secret-Society-Braid/imas-cord-hub-backend-reloaded/wiki) を参照してください。**

このリポジトリでは Docker を用いてローカルの環境でAPIを動かすことができます。

トップディレクトリに置いている `Dockerfile` ファイルを用いて、Dockerイメージをビルドすることができます。

```bash
$ docker build -t imas-cord-hub-backend-reloaded:latest .
```

ビルドが完了し次第、Docker CLIや Docker Desktopなりでイメージを起動してください。`Dockerfile`
より`8080`番ポートを公開するよう設定していますので、お好きなポートへフォワードしてください。

```bash
$ docker run -d -p 8080:8080 imas-cord-hub-backend-reloaded:latest
```

## 機能のコントリビューション

基本的にオープンにしていますので、自由にフォークし、PRを `main` ブランチに向けて送ってください。その際、関連するIssueがあれば **その番号を説明に付記する** ようお願いいたします。ない場合は変更点の概要などでかまいません。

## ライセンス

このリポジトリのコード部分は AGPL-3.0 ライセンスの下で公開されています。詳細は [LICENSE](https://Secret-Society-Braid/imas-cord-hub-backend-reloaded/blob/main/LICENSE) を参照してください。

しかしながら、外部へ公開しているAPIが提供しているサーバーとファンサイトに関するデータは `CC-BY-NC-SA 4.0` として公開しています。ライセンスにご注意ください。

## 連絡先

何かご不明点などがあればこのリポジトリの Discussions に投稿していただくようお願いいたします。
