import React from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import Earth from '@/components/ui/Earth';

export default function CanvasWrapper(): JSX.Element {
  return (
    <Canvas style={{ height: '150px' }}>
      <ambientLight intensity={1.5} />
      <directionalLight position={[10, 10, 5]} intensity={2} />
      <Earth />
      <OrbitControls />
    </Canvas>
  );
}
