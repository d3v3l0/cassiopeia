(ns cassiopeia.destination.flatiron
"
.-. .   .-. .-. .-. .-. .-. .  .
|-  |   |-|  |   |  |(  | | |\\|
'   `-' ` '  '  `-' ' ' `-' ' ``"
  (:use overtone.live)
  (:use cassiopeia.engine.core)
  (:use cassiopeia.waves.synths)
  (:use cassiopeia.samples)
  (:use cassiopeia.engine.buffers)
  (:use cassiopeia.dirt)
  (:require [cassiopeia.engine.timing :as time]
            [clojure.math.numeric-tower :as math]
            [overtone.studio.fx :as fx]
            [shadertone.tone :as t]))

(def master-vol 3.0)
(volume master-vol)
(ctl time/root-s :rate 8.)

;;START

(do
  (defbufs 256 [df-b note1-dur-b sd-attack-b sd-release-b sd-amp-b s-note-b])

  (defonce grumblers-g (group "the grumblers"))
  (kill heart-wobble)
  (def grumble-chord-g
    (chord-synth heart-wobble 4 [:head grumblers-g] :amp 0 :dur-buf note1-dur-b :beat-bus (:count time/beat-1th) :beat-trg-bus (:beat time/beat-1th) :lag-time 0.0 :t 0.0))

  (def grumble-chords
    (do
      (let [_ [0 0 0]
            [F21 F22 F23 F24 F25 F26 F27] (map #(chord-degree %1 :F1 :minor 3) [:i :ii :iii :iv :v :vi :vii])
            [F31 F32 F33 F34 F35 F36 F37] (map #(chord-degree %1 :F3 :minor 3) [:i :ii :iii :iv :v :vi :vii])
            [F41 F42 F43 F44 F45 F46 F47] (map #(chord-degree %1 :F4 :minor 4) [:i :ii :iii :iv :v :vi :vii])
            [Fa31 Fa32 Fa33 Fa34 Fa35 Fa36 Fa37] (map #(chord-degree %1 :F3 :minor 4) [:i :ii :iii :iv :v :vi :vii])
            [Fa41 Fa42 Fa43 Fa44 Fa45 Fa46 Fa47] (map #(chord-degree %1 :F3 :major 4) [:i :ii :iii :iv :v :vi :vii])
            [C41 C42 C43 C44 C45 C46 C47] (map #(chord-degree %1 :C4 :minor3) [:i :ii :iii :iv :v :vi :vii])
            [C31 C32 C33 C34 C35 C36 C37] (map #(chord-degree %1 :C3 :minor3) [:i :ii :iii :iv :v :vi :vii])]

        (pattern! note1-dur-b [12 12 12 1/2 12 12 12 1/2])
        (let [chord-pat
              (concat
               (repeat 12 [0]) [F22 F21  _ _]
               (repeat 12 [0]) [_ _  _ _])]
          (chord-pattern  grumble-chord-g chord-pat))))
    )

  (def apeg-deep-melody-spair2-chord-g
    (chord-synth general-purpose-assembly 4 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th) :attack 0.1 :release 0.1))

  (def apeg-deep-melody-spair-chord-g
    (chord-synth general-purpose-assembly 4 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th) :attack 0.1 :release 0.1))
  (def apeg-deep-melody-chord-g
    (chord-synth general-purpose-assembly 4 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-1th) :beat-bus (:count time/beat-1th) :attack 0.1 :release 0.1))
  (def main-melody-chord-g
    (chord-synth general-purpose-assembly 3 :amp 0.0 :noise-level 0.05 :beat-trg-bus (:beat time/beat-2th) :beat-bus (:count time/beat-2th) :attack 0.1 :release 0.1))

  (comment
    (map #(ctl %1 :saw-cutoff 800) slow-deep-chord-group)
    (map #(ctl %1 :beat-trg-bus (:beat time/beat-4th) :beat-bus (:count time/beat-4th)) slow-deep-chord-group)
    (map #(ctl %1 :saw-cutoff 800 :release 6 :noise 100.2 :attack 0.4 :amp 0.05) slow-deep-chord-group)
    (doseq [chord-g slow-deep-chord-group] (ctl chord-g :saw-cutoff 1000 :amp 0.03 :attack 0.1 :noise-level 0.05 :release 1.0 :beat-trg-bus (:beat time/beat-2th) :wave 2 :beat-bus (:count time/beat-2th)))
    )

  ;;(kill general-purpose-assembly-pi)
  (defonce sd-g (group "slow deep chords"))
  (def slow-deep-chord-g
    ;;Needs 4
    (chord-synth general-purpose-assembly-pi 4 [:head sd-g] :amp-buf sd-amp-b :release-buf sd-release-b :attack-buf sd-attack-b :saw-cutoff 0 :attack 0.3 :release 6.0 :amp 0.2 :noise-level 0.05 :beat-trg-bus (:beat time/beat-2th) :beat-bus (:count time/beat-2th)
)))

(map #(map find-note-name %1) (map #(chord-degree %1 :F2 :major 4) [:i :ii :iii :iv :v :vi :vii]))
(map #(map find-note-name %) (chords-with-inversion [1 2] :F2 :minor :up 4))
(map #(map find-note-name %) (chords-with-inversion [1] :F2 :minor :up 4))

(def dark-chords-score
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27]        (chords-for :C2 :minor 3)
        [f217 f227 f237 f247 f257 f267 f277] (chords-for :F2 :melodic-minor-asc 4)
        [fm31 fm32 fm33 fm34 fm35 fm36 fm37] (chords-for :F3 :major 4)

        [f31 f32 f33 f34 f35 f36 f37]        (chords-for :F3 :minor 3)
        [f317 f327 f337 f347 f357 f367 f377] (chords-for :F3 :minor 4)

        [f417 f427 f437 f447 f457 f467 f477] (chords-for :F4 :minor 4)

        [f41 f42 f43 f44 f45 f46 f47]        (chords-for :F4 :minor 3)
        [fu21 fu22 fu23 fu24 fu25 fu26 fu27]          (chords-with-inversion [1] :F2 :minor :up 3)
        [fuu21 fuu22 fuu23 fuu24 fuu25 fuu26 fuu27]   (chords-with-inversion [1 2] :F2 :minor :up 3)
        [f3ii21 f3ii22 f3ii23 f3ii24 f3ii25 f3ii26 f3ii27]   (chords-with-inversion [1 2] :F2 :minor :down 3)
        [fii217 fii227 fii237 fii247 fii257 fii267 fii277]   (chords-with-inversion [1 2] :F2 :minor :down 4)
        [fii21 fii22 fii23 fii24 fii25 fii26 fii27]   (chords-with-inversion [1 2] :F2 :minor :down 3)
        [fi11 fi12 fi13 fi14 fi15 fi16 fi17]          (chords-with-inversion [1 2] :F2 :minor :down)
        [fi21 fi22 fi23 fi24 fi25 fi26 fi27]          (chords-with-inversion [1]   :F2 :minor :down)
        [f21 f22 f23 f24 f25 f26 f27]                 (chords-for :F2 :minor 3)
        [fmm21 fmm22 fmm23 fmm24 fmm25 fmm26 fmm27]   (chords-with-inversion [1] :F2 :melodic-minor :down)
        [fi31 fi32 fi33 fi34 fi35 fi36 fi37]          (chords-with-inversion [1] :F3 :minor :down)
        [fii31 fii32 fii33 fii34 fii35 fii36 fii37]   (chords-with-inversion [1 2] :F3 :minor :down)
        [fma21 fma22 fma23 fma24 fma25 fma26 fma27] (chords-with-inversion [] :F2 :melodic-minor-asc :up 3)
        [fmd21 fmd22 fmd23 fmd24 fmd25 fmd26 fmd27] (chords-with-inversion [] :F2 :melodic-minor-desc :up 3)

        all (chord-degree :ii :F3 :melodic-minor-asc)]
    (let [_ (pattern! sd-attack-b  [0.2];;(repeat 2 [0.06 0.32 0.32 0.32 0.32 0.32 0.32 0.32]) (repeat 2 [0.06 0.32 0.32 0.32])
                      )
          _ (pattern! sd-release-b [1.0];; [1.0  1.0 1.0 1.0]
                      )
          _ (pattern! sd-amp-b     [1];;  (repeat 2 [1.1  0.9 0.9 0.9 0.9 0.9 0.9 0.9]) (repeat 2 [1.1 0.9 0.9 0.9])
                      )
          chord-pat
          [
           f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31
           f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33
           f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34
           f36 f36 f36 f36 f36 f36 f36 f36
           (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5) (chord :F3 :m+5)

           f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31 f31
           f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33 f33
           f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34 f34
           f36 f36 f36 f36 f36 f36 f36 f36
           (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5) (chord :F3 :m7+5)]]
      (chord-pattern slow-deep-chord-g chord-pat))))

(def darker-pinger-score
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27]  (chords-for :C2 :minor 1)
        [c31 c32 c33 c34 c35 c36 c37]  (chords-for :C3 :minor 1)
        [c41 c42 c43 c44 c45 c46 c47]  (chords-for :C4 :minor 1)
        [f21 f22 f23 f24 f25 f26 f27]  (chords-for :F2 :minor 1)
        [f31 f32 f33 f34 f35 f36 f37]  (chords-for :F3 :minor 1)
        [f41 f42 f43 f44 f45 f46 f47]  (chords-for :F4 :minor 1)
        [f51 f52 f53 f54 f55 f56 f57]  (chords-for :F5 :minor 1)
        ]
    (let [chord-pat
          [
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c37 f31 f33 f34    f31 f31 f41 f31  c37 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c34 f31 f33 f31    f31 f31 f41 f31

           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f34    f31 f31 f41 f31  c41 f31 f33 f34    f31 f31 f41 f31
           c37 f31 f33 f34    f31 f31 f41 f31  c37 f31 f33 f34    f31 f31 f41 f31
           c41 f31 f33 f41    f27 f31 f31 f31  c31 f31 f33 f41    f31 f31 f41 f31
           ]]
      (chord-pattern apeg-deep-melody-chord-g chord-pat))))

(def apeg-swell
  (let [_ [nil nil nil]
        chord-pat [(degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3)

                   (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3) (degrees [1] :minor :F3)

                   (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3)

                   (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3)
                   (degrees [5] :minor :F3) (degrees [5] :minor :F3) (degrees [5] :minor :F3) (degrees [5] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3) (degrees [4] :minor :F3)
                   ]]
    (chord-pattern main-melody-chord-g chord-pat)))

(def chords-score
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27]        (chords-for :C2 :minor 3)
        [f217 f227 f237 f247 f257 f267 f277] (chords-for :F2 :melodic-minor-asc 4)
        [fm21 fm22 fm23 fm24 fm25 fm26 fm27] (chords-for :F2 :major 4)
        [f31 f32 f33 f34 f35 f36 f37]        (chords-for :F3 :minor 3)
        [f41 f42 f43 f44 f45 f46 f47]        (chords-for :F4 :minor 3)
        [fu21 fu22 fu23 fu24 fu25 fu26 fu27]          (chords-with-inversion [1] :F2 :minor :up 3)
        [fuu21 fuu22 fuu23 fuu24 fuu25 fuu26 fuu27]   (chords-with-inversion [1 2] :F2 :minor :up 3)
        [f3ii21 f3ii22 f3ii23 f3ii24 f3ii25 f3ii26 f3ii27]   (chords-with-inversion [1 2] :F2 :minor :down 3)
        [fii217 fii227 fii237 fii247 fii257 fii267 fii277]   (chords-with-inversion [1 2] :F2 :minor :down 4)
        [fii21 fii22 fii23 fii24 fii25 fii26 fii27]   (chords-with-inversion [1 2] :F2 :minor :down 3)
        [fi11 fi12 fi13 fi14 fi15 fi16 fi17]          (chords-with-inversion [1 2] :F2 :minor :down)
        [fi21 fi22 fi23 fi24 fi25 fi26 fi27]          (chords-with-inversion [1]   :F2 :minor :down)
        [f21 f22 f23 f24 f25 f26 f27]                 (chords-for :F2 :minor 3)
        [fmm21 fmm22 fmm23 fmm24 fmm25 fmm26 fmm27]   (chords-with-inversion [1] :F2 :melodic-minor :down)
        [fi31 fi32 fi33 fi34 fi35 fi36 fi37]          (chords-with-inversion [1] :F3 :minor :down)
        [fii31 fii32 fii33 fii34 fii35 fii36 fii37]   (chords-with-inversion [1 2] :F3 :minor :down)

        f317 (first (chords-for :F3 :minor 4))
        f257 (first (chords-for :F2 :minor 4))

        f41dim (chord :F3 :a)
        f42dim (chord :G2 :7sus4)
        f43dim (chord :A3 :dim)
        f44dim (chord :F3 :7sus4)
        f45dim (chord :C3 :7sus4)
        f46dim (chord :D3 :7sus4)
        f47dim (chord :E3 :7sus4)

        sus4 (chord :F3 :dim)

        f2iisus4 (chord :F2 :sus4 2)
        f2isus4 (chord :F2 :sus4 1)
        f2isus2 (chord :F2 :sus2 1)
        f2iisus2 (chord :F2 :sus2 2)

        [fma21 fma22 fma23 fma24 fma25 fma26 fma27] (chords-with-inversion [] :F2 :melodic-minor-asc :up 3)
        [fmd21 fmd22 fmd23 fmd24 fmd25 fmd26 fmd27] (chords-with-inversion [] :F2 :melodic-minor-desc :up 3)
        ;;[fma21 fma22 fma23 fma24 fma25 fma26 fma27] (chords-for :F2 :melodic-minor)

        all (chord-degree :ii :F3 :melodic-minor-asc)]
    (let [_ (pattern! sd-attack-b  [0.06 0.12 0.12 0.12])
          _ (pattern! sd-release-b [1.0  1.0 1.0 1.0])
          _ (pattern! sd-amp-b     [1.2  1.0 1.0 1.0])

          chord-pat
          [fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21
           f26 f26 f26 f26 f26 f26 f26 f26
           fu23 fu23 fu23 fu23 fu23 fu23 fu23 fu23
           fu24 fu24 fu24 fu24 fu24 fu24 (chord :F2 :sus4 2) (chord :F2 :sus4 2)

           fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21
           fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21 fuu21
           ;;f26 f26 f26 f26 f26 f26 f26 f26   f26 f26 f26 f26 f26 f26 f26 f26
           fu23 fu23 fu23 fu23 fu23 fu23 fu23 fu23
           fu25 fu25 fu25 fu25 fu25 fu25  (chord :F2 :7sus4 2) (chord :F2 :7sus4 2)
           ]]
      (chord-pattern slow-deep-chord-g chord-pat ))))

(defn as-chord [note] (flatten [note 0 0 0]))

(def pinger-score-alternative
  (let [_ [0 0 0 0]
         [c21 c22 c23 c24 c25 c26 c27] (chords-for :C2 :minor 1)
         [c31 c32 c33 c34 c35 c36 c37] (chords-for :C3 :minor 1)
         [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
         [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
         [f21 f22 f23 f24 f25 f26 f27] (chords-for :F2 :minor 1)
         [f31 f32 f33 f34 f35 f36 f37] (chords-for :F3 :minor 1)
         [f41 f42 f43 f44 f45 f46 f47] (chords-for :F4 :minor 1)
         [f31i f32i f33i f34i f35i f36i f37i] (chords-with-inversion [1 2] :F3 :minor 1)]

    [f43 _   f43 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     f41 f43 f41 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     c41 f35 f31 f34 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     c41 f35 f31 f34 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     f41 _   f43 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     f41 f43 f41 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     f37 f41 _ f41 c35 _ (as-chord (degrees [1] :minor :F3)) (as-chord (degrees [1] :minor :F3))
     f37 f41 _ f41 c41 _ (as-chord (degrees [1] :minor :F3) ) (as-chord (degrees [1] :minor :F3))
     ;;--
     f41 _   f43 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     f41 f43 f41 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     f41 _   f43 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     f41 f43 f41 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     f41 _   f43 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))
     f41 f43 f41 f44 f37 f36 (as-chord (degrees [7] :minor :F3)) (as-chord (degrees [7] :minor :F3))

     f37 f41 _ f41 c35 _  (as-chord (degrees [1] :minor :F3)) (as-chord (degrees [1] :minor :F3))
     f37 f41 _ f41 c41 _  (as-chord (degrees [1] :minor :F3)) (as-chord (degrees [1] :minor :F3))
     ]
    ))

(chord-pattern apeg-deep-melody-spair2-chord-g pinger-score-alternative)
(map #(ctl % :amp 0.4) (:synths apeg-deep-melody-spair2-chord-g))
(map #(ctl % :amp 0.4) (:synths apeg-deep-melody-spair-chord-g))

(def pinger-score-highlighted
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27] (chords-for :C2 :minor 1)
        [c31 c32 c33 c34 c35 c36 c37] (chords-for :C3 :minor 1)
        [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
        [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
        [f21 f22 f23 f24 f25 f26 f27] (chords-for :F2 :minor 1)
        [f31 f32 f33 f34 f35 f36 f37] (chords-for :F3 :minor 1)
        [f41 f42 f43 f44 f45 f46 f47] (chords-for :F4 :minor 1)]
    [ f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 f37 c36 (flatten [(degrees [1] :minor :F4) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 [(degrees [1] :minor :F3) 0 0 0] (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 [(degrees [1] :minor :F3) 0 0 0] (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])

     f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
     f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])])
)

(def pinger-score
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27] (chords-for :C2 :minor 1)
        [c31 c32 c33 c34 c35 c36 c37] (chords-for :C3 :minor 1)
        [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
        [c41 c42 c43 c44 c45 c46 c47] (chords-for :C4 :minor 1)
        [f21 f22 f23 f24 f25 f26 f27] (chords-for :F2 :minor 1)
        [f31 f32 f33 f34 f35 f36 f37] (chords-for :F3 :minor 1)
        [f41 f42 f43 f44 f45 f46 f47] (chords-for :F4 :minor 1)

        [f31i f32i f33i f34i f35i f36i f37i] (chords-with-inversion [1 2] :F3 :minor 1)
        ]
    (let [chord-pat
          [
           f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 f37 c36 (flatten [(degrees [1] :minor :F4) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 [(degrees [1] :minor :F3) 0 0 0] (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 [(degrees [1] :minor :F3) 0 0 0] (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 f37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])

           ;; f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])

           ;; c41 f43 f41 f41 _   c36 (flatten [(degrees [7] :minor :F3) (degrees [1] :minor :F4) 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ;; c41 f41 f31 f41 c47 c46 (flatten [(degrees [7] :minor :F3) (degrees [1] :minor :F4) 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ]
          ]
      (chord-pattern apeg-deep-melody-chord-g chord-pat))))

(def pinger-growth-score-spair
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27] (chords-for :C2 :minor 1)
        [c31 c32 c33 c34 c35 c36 c37] (chords-for :C3 :minor 1)
        [f21 f22 f23 f24 f25 f26 f27] (chords-for :F2 :minor 1)
        [f31 f32 f33 f34 f35 f36 f37] (chords-for :F3 :minor 1)
        [f41 f42 f43 f44 f45 f46 f47] (chords-for :F4 :minor 1)]
    (let [chord-pat
          [f41 _   f43 f44 f37 f36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           f41 f43 f41 f44 f37 f36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           f41 f43 f41 f44 f37 f36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           f41 f43 f41 f44 f37 f36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])
           ]]
      (chord-pattern apeg-deep-melody-spair-chord-g chord-pat))))

(def pinger-score-spair
  (let [_ [0 0 0 0]
        [c21 c22 c23 c24 c25 c26 c27] (chords-for :C2 :minor 1)
        [c31 c32 c33 c34 c35 c36 c37] (chords-for :C3 :minor 1)
        [f21 f22 f23 f24 f25 f26 f27] (chords-for :F2 :minor 1)
        [f31 f32 f33 f34 f35 f36 f37] (chords-for :F3 :minor 1)
        [f41 f42 f43 f44 f45 f46 f47] (chords-for :F4 :minor 1)
        chord-pat [f41 f43 f41 f44 c37 c36 (flatten [(degrees [7] :minor :F3) 0 0 0]) (flatten [(degrees [7] :minor :F3) 0 0 0])]]
    (chord-pattern apeg-deep-melody-spair-chord-g chord-pat)))

(do
  (kill crackle-snail)
  (crackle-snail :noise-level 0.1 :amp 0.6 :note-buf s-note-b)
  (pattern! s-note-b [(degrees [1] :minor :F1) (degrees [3] :minor :F1) 0 (degrees [4] :minor :F1) (degrees [1] :minor :F1) 0 0 0] (repeat 24 [0])))

(do (defonce drums-g (group "drums")) (defonce drum-effects-g (group "drums effects for extra sweetness")) (defbufs 128 [bass-notes-buf bass-notes2-buf hats-buf kick-seq-buf white-seq-buf effects-seq-buf effects2-seq-buf bass-notes-buf effects3-seq-buf]))

(pattern! bass-notes-buf
          (repeat 8 (degrees [1] :minor :F1))
          (repeat 2 (repeat 8 (degrees [1] :minor :F1)))
          (repeat 2 (repeat 8 (degrees [3] :minor :F1)))
          (repeat 2 (repeat 8 (degrees [3] :minor :F1)))
          [(degrees [1 1 1 1  5 4 3 1] :minor :F1)])

(pattern! effects2-seq-buf [1 1 0 0 0 0 0 0])
(pattern! effects2-seq-buf [1 1 1  1 0 0  0 1 0  0 0 0  0 0 0])
(pattern! effects2-seq-buf [1 0 0  1 1 1  0 0 0  1 0 0])
(pattern! effects-seq-buf  (repeat 12 1)  [1 0 0 0])
(pattern! effects2-seq-buf (repeat (* 8 8) 0)
                           (repeat (* 8 7) 0) [0 0 0 0 0 1 1 1])

(defonce kick-fuzzy-s (freesound-sample 168415))
(def ballon-perc-s (freesound-sample 168301))

;;(kill seqer)

(def fuzzy-kick-drums (doall (map #(seqer [:head drum-effects-g]
                                          :rate-start 0.2 :rate-limit 0.3
                                          :beat-num %1 :pattern effects2-seq-buf :amp 0.035 :num-steps 8 :buf kick-fuzzy-s) (range 0 16))))

(kill drum-effects-g)
(kill drums-g)

(one-time-beat-trigger
 15 16
 (fn []
   (do
     (pattern! hats-buf      [0 0 0 0 1 0 0 0   0 0 1 0 0 0 0 0])
     (pattern! kick-seq-buf  [1 0 0 1 0 0 0 0   1 0 0 0 0 0 0 0])

     (def white (doall (map #(whitenoise-hat [:head drums-g] :amp 1.0 :seq-buf hats-buf :beat-bus (:count time/beat-1th) :beat-trg-bus (:beat time/beat-1th) :num-steps 24 :release 0.1 :attack 0.0 :beat-num %1) (range 0 24))))
     (ctl white :attack 0.04 :release 0.01 :amp 2)
     (ctl white :attack 0.002 :release 0.04 :amp 2)

     (def kicker (doall (map #(space-kick2 [:head drums-g] :note-buf bass-notes-buf :seq-buf  kick-seq-buf :num-steps 32 :beat-num %1 :noise 0.05 :amp 4.2 :mod-index 0.1 :mod-freq 4.0 :mode-freq 0.2) (range 0 32))))
     (ctl kicker :attack 0.0 :sustain 0.2 :amp 1.0)
     )))

(one-time-beat-trigger
 0 16
 (fn []
   (doseq [s (:synths apeg-deep-melody-chord-g)]
     (ctl s :amp 0.00 :saw-cutoff 2000 :wave 0 :attack 1.0 :release 5.0)
     (n-overtime! s :amp 0.0 0.019 0.001))
   ))

;(grainy-buf :b (buffer-mix-to-mono rf-fx-s) :amp 0.3 :dur 5.0 :trate 1 :amp 0.9)
(echoey-buf rf-theorems-s :amp 0.1)

(doseq [chord-g (:synths slow-deep-chord-g)]
  (ctl chord-g :saw-cutoff 300 :amp 0.0 :attack 0.1 :noise-level 0.05 :release 1.0 :wave 4)
  (n-overtime! chord-g :amp 0.0 0.04 0.001)
  )

(def hand-drums (doall (map #(seqer [:head drum-effects-g] :beat-num %1 :pattern effects-seq-buf :amp 0.25 :num-steps 16 :buf hand-drum-s :rate-start 1.0 :rate-limit 1.0) (range 0 16))))

(comment
  (map #(ctl %1 :saw-cutoff 1000 :noise-level 0.5 :amp 0.09 :attack 0.3 :release 6.0 :beat-trg-bus (:beat time/beat-4th) :beat-bus (:count time/beat-4th)) (:synths slow-deep-chord-g))
  (map #(ctl %1 :t 0.005 :amp 0.4) (:synths grumble-chord-g))
  (map #(ctl %1 :saw-cutoff 900) (:synths slow-deep-chord-g)))

(pattern! hats-buf      [0 0 0 0 1 0 0 0   0 0 1 0 0 0 0 0])
(pattern! kick-seq-buf  [1 0 0 1 0 0 0 0   1 0 0 0 0 0 0 0])

(pattern! kick-seq-buf [1 0 0 0 0 0 0 0])
(pattern! kick-seq-buf [1 0 0 0])
(pattern! kick-seq-buf [1 0 0])
(pattern! kick-seq-buf [1 0])
(pattern! kick-seq-buf [1 0])
(pattern! hats-buf [1])

(pattern! kick-seq-buf
          [1 0 0 0 1 0 0 0] [1 0 0 0 1 0 0 0] [1 0 0 0 1 0 0 0] [1 0 0 0 1 0 0 0]
          [1 0 0 0 0 0 0 0] [1 0 0 0 1 0 0 0] [1 0 0 0 0 0 0 0] [1 0 0 0 1 1 1 1])

(pattern! kick-seq-buf
          [1 0 0 0 1 0 0 0] [1 1 0 0 1 0 0 0] [1 0 0 0 1 0 0 0] [1 1 0 0 1 0 0 0]
          [1 0 0 0 1 0 0 0] [1 1 0 0 1 0 0 0] [1 0 0 0 1 0 0 0] [1 1 0 0 1 0 0 0])

(pattern! hats-buf
          [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 1 0 0 1 0]
          [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 1 0 0 1 0])

(pattern! hats-buf
          [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0]
          [1 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0] [1 0 1 0 0 0 1 0] [0 0 1 0 0 0 1 0])


(map #(ctl % :wave 0 :amp 0.1) (:synths apeg-deep-melody-spair-chord-g))
(map #(ctl % :saw-cutoff 2500 :wave 4) (:synths apeg-deep-melody-chord-g))
(map #(do (ctl % :amp 0.00 :saw-cutoff 2000 :wave 0 :attack 1.0 :release 5.0)
          (n-overtime! % :amp 0 0.019)) (:synths apeg-deep-melody-chord-g))

;;(ctl apeg-deep-melody-chord-g :amp 0.06 :saw-cutoff 1000 :wave 1 :attack 1.0 :release 5.0)
;;(set-beat apeg-deep-melody-chord-g time/beat-1th)

(pattern! (nth (:bufs apeg-deep-melody-chord-g) 1)
          [0 (degrees [7] :minor :F3) (degrees [6] :minor :F3) 0]
          [0 (degrees [7] :minor :F3) (degrees [6] :minor :F3) 0]
          [0 (degrees [7] :minor :F3) (degrees [1] :minor :F4) 0]
          [0 (degrees [7] :minor :F3) (degrees [1] :minor :F4) 0]

          [0 (degrees [7] :minor :F3) (degrees [7] :minor :F3) 0]
          [0 (degrees [7] :minor :F3) (degrees [7] :minor :F3) 0]
          [0 (degrees [7] :minor :F3) (degrees [1] :minor :F4) 0]
          [0 (degrees [7] :minor :F3) (degrees [1] :minor :F4) 0]
          )

;;(ctl (foundation-output-group) :master-volume 4)

;;(on-beat-trigger 256 #(echoey-buf pulse-s :amp 0.02))
;;(on-beat-trigger 128 #(echoey-buf godzilla-s :amp 0.4))
;;(on-beat-trigger 64 #(spacy constant-blues-s :amp 0.5))

;;(on-beat-trigger 32 #(spacy (dirt :kurt 1)))
;;(on-beat-trigger 64 #(spacy (dirt :kurt 3)))
;;(on-beat-trigger 128 #(spacy (dirt :kurt 2)))

;;(on-beat-trigger 256 #(echoey-buf ooo-s :amp 0.04))

;;(sample-trigger #(spacy (dirt :kurt 3)   :amp 0.5) 7  32)
;;(sample-trigger #(spacy (dirt :kurt 2)   :amp 0.5) 15 32)
;;;;(sample-trigger #(spacy (dirt :kurt 4) :amp 0.5) 17 32)
;;(sample-trigger #(spacy (dirt :kurt 5)   :amp 0.5) 1 32)
;;(sample-trigger #(spacy (dirt :kurt 6)   :amp 0.5) 9 32)

;;(remove-all-beat-triggers)
;;(remove-all-sample-triggers)
;;(mono-player (dirt :pad 2) :amp 0.2)
;;(mono-player rf-beat-it-s :amp 0.2)
;;(spacy (dirt :cosmicg 2) :amp 0.5)
;;(on-beat-trigger 8 #(spacy (dirt :voodoo 0)))
;;(on-beat-trigger 64 #(echoey-buf (dirt :wind) :amp 0.1))
;;(on-beat-trigger 32 #(echoey-buf (dirt :wind) :amp 0.1))
;;(on-beat-trigger 16 #(echoey-buf (dirt :wind) :amp 0.1))
;;(on-beat-trigger 8 #(echoey-buf (dirt :wind) :amp 0.1))

;;(sample-trigger #(do (echoey-buf (dirt :kurt 6) :amp 0.11)) 31 32)
;;(remove-all-sample-triggers)

(do ;;shh drums
  (ctl drum-effects-g :amp 0.0)
  (ctl drums-g :amp 1.0))

(spacy (dirt :pad 0) :amp 1.0)
(pattern! hats-buf [1])

(pattern! df-b [(degrees [1 1 1 1] :minor :F3)
                (degrees [1 1 1 1] :minor :F3)
                (degrees [1 1 1 1] :minor :F3)
                ;;(degrees [6 6 6 6] :minor :F3)
                ;;(degrees [7 7 7 7] :minor :F3)
                (degrees [1 1 1 1] :minor :F4)
                ])

(one-time-beat-trigger 126 128 #(do
                                  ;;                                  (chord-pattern apeg-deep-melody-chord-g pinger-score-alternative)
                                  (chord-pattern apeg-deep-melody-chord-g pinger-score-highlighted)

                                  (plain-space-organ :tone (/ (midi->hz (note :F1)) 2) :duration 3 :amp 0.25)))

(one-time-beat-trigger
 126 128
 (fn [] ;;DARKER PROGRESSION
   (do
     (doseq [s (:synths apeg-deep-melody-chord-g)] (ctl s :amp 0.00))

     (plain-space-organ :tone (/ (midi->hz (note :F1)) 2) :duration 3 :amp 0.5)
     (ctl drum-effects-g :amp 0.0)
     (ctl drums-g :amp 0.0)

     (chord-pattern slow-deep-chord-g dark-chords-score )
     (chord-pattern apeg-deep-melody-chord-g darker-pinger-score)
     )
   (doseq [s (:synths apeg-deep-melody-chord-g)] (ctl s :amp 0.00 :saw-cutoff 100 :wave 0 :attack 1.0 :release 5.0)
          (n-overtime! s :saw-cutoff 100 2000 50)
          (n-overtime! s :amp 0.00 0.04 0.005))
   ;;(doseq [s (:synths apeg-deep-melody-chord-g)] (ctl s :amp 0.00 :saw-cutoff 2000 :wave 0 :attack 1.0 :release 5.0) (n-overtime! s :amp 0.00 0.04 0.005))
   ))

;;Drive home home chords + highlight melody
(doseq [s (:synths main-melody-chord-g)] (ctl s :amp 0.09 :saw-cutoff 450 :wave 1 :attack 1.0 :release 5.0))
(doseq [s (:synths apeg-deep-melody-chord-g)] (ctl s :amp 0.03 :saw-cutoff 2900))

;;Drum tension
(pattern! hats-buf [1])
(map #(ctl % :amp 1.0) white)
(pattern! kick-seq-buf [1 0 0 0 1 0 0 0])
(map #(ctl % :amp 1.0) kicker)

(do
  (doseq [s (:synths main-melody-chord-g)] (ctl s :amp 0.0))
  (doseq [s (:synths apeg-deep-melody-spair-chord-g)]
    (ctl s :amp 0.00 :saw-cutoff 2000 :wave 2 :attack 1.0 :release 5.0)
    (n-overtime! s :amp 0.0 0.04 0.01)
    )

  (chord-pattern apeg-deep-melody-spair-chord-g pinger-growth-score-spair)

  (ctl drum-effects-g :amp 0.6) (ctl drums-g :amp 1.0) (ctl fuzzy-kick-drums :amp 0.035)
  (doseq [s (:synths apeg-deep-melody-chord-g)] (ctl s :amp 0.05 :saw-cutoff 2600 :wave 0 :attack 1.0 :release 5.0))
  (def f (dulcet-fizzle :amp 2.0 :note-buf df-b))
  ;;  (pattern! hats-buf [1 0 0 0 0 0 0 0])
  )

(do
  (doall (map #(ctl % :amp 0) (:synths apeg-deep-melody-spair-chord-g)))
  (doall (map #(set-beat % time/beat-2th) (:synths apeg-deep-melody-spair-chord-g)))
  (doall (map #(set-beat % time/beat-2th) (:synths apeg-deep-melody-chord-g)))
  (doall (map #(set-beat % time/beat-1th) (:synths slow-deep-chord-g)))

  (chord-pattern slow-deep-chord-g pinger-score)

  (let [_ (pattern! sd-attack-b  [0.06 0.12 0.12 0.12])
        _ (pattern! sd-release-b [1.0  0.6 0.4 0.2])
        _ (pattern! sd-amp-b     [1.2  1.0 1.0 1.0])]
    (chord-pattern apeg-deep-melody-chord-g chords-score)))

(do
  (doall (map #(set-beat % time/beat-1th) (:synths apeg-deep-melody-chord-g)))
  (doall (map #(set-beat % time/beat-1th) (:synths apeg-deep-melody-spair-chord-g)))
  (doall (map #(set-beat % time/beat-2th) (:synths slow-deep-chord-g)))

  (doall (map #(ctl % :amp 0.3) (:synths apeg-deep-melody-spair2-chord-g)))
  (chord-pattern apeg-deep-melody-spair-chord-g pinger-growth-score-spair)
  (chord-pattern apeg-deep-melody-spair2-chord-g pinger-score-alternative)
  (chord-pattern apeg-deep-melody-chord-g darker-pinger-score)

  (chord-pattern apeg-deep-melody-chord-g pinger-score-highlighted)

  (let [_ (pattern! sd-attack-b  [0.06 0.12 0.12 0.12])
        _ (pattern! sd-release-b [1.0  1.0 1.0 1.0])
        _ (pattern! sd-amp-b     [1.2  1.0 1.0 1.0])]
    (chord-pattern slow-deep-chord-g chords-score))
  )

(pattern! hats-buf [0])
(pattern! hats-buf [1])
(pattern! hats-buf [0 0 0 0 1 0 0 0   0 0 1 0 0 0 0 0])

(do (ctl drum-effects-g :amp 1.0) (ctl drums-g :amp 1.0))

;;(map #(ctl % :saw-cutoff 2800 :amp 0.04) (:synths apeg-deep-melody-chord-g))

;;(on-beat-trigger 64 #(do (plain-space-organ :tone (/ (midi->hz (note :F2)) 2) :duration 3 :amp 0.2)))

(remove-all-beat-triggers)

;;Fade
(let [cutout 2000]
  (ctl drum-effects-g :amp 0)
  (doall (map #(ctl % :saw-cutoff cutout :amp 0.03) (:synths apeg-deep-melody-spair-chord-g)))
  (doall (map #(ctl % :saw-cutoff cutout :amp 0.03) (:synths apeg-deep-melody-chord-g)))
  (doall (map #(ctl % :saw-cutoff cutout :amp 0.03) (:synths slow-deep-chord-g))))

(echoey-buf rf-trig-s :amp 0.1 :decay 8 :delay 0.9)
(spacy rf-full-s :amp 0.9)
;;(echoey-buf rf-full-s :amp 0.04)

(comment
  ;;(ctl (foundation-output-group) :master-volume 3)
  (ctl drums-g :amp 0)
  (ctl drum-effects-g :amp 0)

  (stop-all-chord-synth-buffers)
  (remove-all-beat-triggers)
  (remove-all-sample-triggers)
  (stop)
  (kill heart-wobble)

  (fadeout-master master-vol)
  (recording-start "~/Desktop/flatiron21.wav")
  (recording-stop)

  (t/start-fullscreen "resources/shaders/manhattan.glsl"
                      :textures [:overtone-audio :previous-frame
                                ]
                      )

  )