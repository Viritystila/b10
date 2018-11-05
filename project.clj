(defproject b10 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
                 :injections [(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)]
                 :repositories [["Viritystila" "https://github.com/Viritystila/OpenCV/raw/master"]]
  :dependencies [[org.clojure/clojure "1.9.0"]
  [org.clojure/math.numeric-tower "0.0.4"] [mud "0.1.2-SNAPSHOT"][overtone "0.10.3"][leipzig "0.10.0"][shadertone "0.2.6-SNAPSHOT"]
                                   [opencv/opencv "4.0.0-a-linux"]
                 [opencv/opencv-native "4.0.0-a-linux"]])
